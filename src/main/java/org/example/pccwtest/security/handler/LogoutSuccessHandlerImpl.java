package org.example.pccwtest.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.example.pccwtest.service.UserTokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * Custom implementation of {@link LogoutSuccessHandler} for handling successful logouts.
 * <p>
 * This class defines the behavior that occurs after a user successfully logs out. It is responsible for
 * removing the user's token from the cache.
 * </p>
 */
@Configuration
@AllArgsConstructor
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    private final UserTokenService userTokenService;

    /**
     * Handles actions to be taken upon successful logout.
     * <p>
     * This method removes the user's token from the cache using the {@link UserTokenService}.
     * </p>
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication ) throws IOException {
        userTokenService.deleteTokenFromCache(request);
        response.setStatus( 200 );
        response.setContentType( "application/json" );
        response.setCharacterEncoding( "utf-8" );
        response.getWriter().print( "logout successfully" );
    }
}
