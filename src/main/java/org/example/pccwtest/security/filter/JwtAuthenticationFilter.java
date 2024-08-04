package org.example.pccwtest.security.filter;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.pccwtest.model.LoginUser;
import org.example.pccwtest.service.UserTokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Custom filter for handling JWT (JSON Web Token) authentication.
 * <p>
 * This filter extracts JWT from the request, validates it, and sets the
 * authentication context if the token is valid. It extends {@link OncePerRequestFilter}
 * to ensure that the filter is executed once per request.
 * </p>
 */
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserTokenService userTokenService;

    private final Cache<String, LoginUser> tokenCache;

    /**
     * Processes the JWT from the request and sets the authentication context
     * if the token is valid.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwtToken = userTokenService.extractTokenFromRequest(request);

        if (jwtToken != null && userTokenService.validateToken(jwtToken)) {
            LoginUser loginUser = tokenCache.getIfPresent(jwtToken);
            if (loginUser != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        loginUser, null, loginUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
