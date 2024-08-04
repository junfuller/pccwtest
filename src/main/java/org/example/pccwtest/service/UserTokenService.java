package org.example.pccwtest.service;


import com.github.benmanes.caffeine.cache.Cache;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.pccwtest.model.LoginUser;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * Utility class for handling JWT (JSON Web Token) operations.
 * <p>
 * This class provides methods for generating and validating JWT tokens using the HS256 signature algorithm.
 * It includes checks for token expiration, issuer, and audience to ensure the token's validity.
 * </p>
 */
@Service
@AllArgsConstructor
public class UserTokenService {
    private final Cache<String, LoginUser> tokenCache;

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static final String ISSUER = "TestIssuer";
    private static final String AUDIENCE = "TestAudience";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    /**
     * Validates a JWT token.
     * <p>
     * This method parses the token to extract claims and verifies the token's signature. It also checks that
     * the token has not expired, and that the issuer and audience match expected values.
     * </p>
     *
     * @param token the JWT token to be validated
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    public boolean validateToken(String token) {
        try {
            // Parse the token and validate the signature
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Check the expiration date
            if (claims.getExpiration().before(new Date())) {
                return false;
            }

            if (!ISSUER.equals(claims.getIssuer())) {
                return false;
            }

            if (!AUDIENCE.equals(claims.getAudience())) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Generates a JWT token for a given username.
     * <p>
     * This method creates a JWT token with the specified username as the subject, and sets the issuer, audience,
     * issued date, and expiration date. The token is signed with a secret key.
     * </p>
     *
     * @param username the username to be included in the token
     * @return the generated JWT token
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(ISSUER)
                .setAudience(AUDIENCE)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
                .signWith(key)
                .compact();
    }

    /**
     * Extracts the token from the request's Authorization header.
     * <p>
     * The token is expected to be in the format "Bearer <token>".
     * </p>
     *
     * @param request the {@link HttpServletRequest} object
     * @return the token as a {@link String}, or {@code null} if no token is present
     */
    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(7);
        }
        return null;
    }


    public void deleteTokenFromCache(HttpServletRequest request) {
        tokenCache.invalidate(extractTokenFromRequest(request));
    }
}
