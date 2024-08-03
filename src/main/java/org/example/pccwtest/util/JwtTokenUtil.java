package org.example.pccwtest.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtTokenUtil {

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static final String ISSUER = "TestIssuer";
    private static final String AUDIENCE = "TestAudience";

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

    //Generate token
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
}
