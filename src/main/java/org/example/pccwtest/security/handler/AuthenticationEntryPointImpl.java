package org.example.pccwtest.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

/**
 * Unauthorized Access Handler
 *
 * @author MengJun
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e ) throws IOException {
        String msg = "Request access: " + request.getRequestURI() + ", authentication failed, unable to access system resources";
        response.setStatus( 200 );
        response.setContentType( "application/json" );
        response.setCharacterEncoding( "utf-8" );
        response.getWriter().print( msg );
    }
}
