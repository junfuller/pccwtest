package org.example.pccwtest.config;

import lombok.AllArgsConstructor;
import org.example.pccwtest.security.annotation.Anonymous;
import org.example.pccwtest.security.filter.JwtAuthenticationFilter;
import org.example.pccwtest.security.handler.LogoutSuccessHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Custom security configuration for the application.
 * This class configures HTTP security, including custom access rules,
 * JWT authentication filter, and password encoding.
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class CustomSecurityConfig {
    private final  RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final JwtAuthenticationFilter authenticationTokenFilter;
    private final LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * Configures HTTP security settings.
     * <ul>
     *     <li>Permits requests to URLs annotated with {@link Anonymous}.</li>
     *     <li>Permits access to Swagger API documentation and other static resources.</li>
     *     <li>Requires authentication for all other requests.</li>
     *     <li>Adds a custom JWT authentication filter before the default UsernamePasswordAuthenticationFilter.</li>
     * </ul>
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf( AbstractHttpConfigurer::disable )
                .authorizeHttpRequests(
                        customizer -> {
                            Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();

                            final List<String> urls = new ArrayList<>();
                            map.keySet().forEach(info -> {
                                HandlerMethod handlerMethod = map.get(info);
                                Anonymous method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Anonymous.class);
                                Optional
                                        .ofNullable(method)
                                        .ifPresent(anonymous -> {
                                            var patternsCondition = info.getPatternsCondition();
                                            if(patternsCondition!=null){
                                                urls.addAll(patternsCondition.getPatterns());
                                            }
                                        });
                            });

                            urls.forEach( url -> customizer.requestMatchers( url ).permitAll() );

                            // Permit Swagger API documentation
                            customizer.requestMatchers( "/swagger-ui/**", "/v3/api-docs/**", "/*.html", "/*.ico", "/error" ).permitAll();
                            customizer.anyRequest().authenticated();
                        }
                )
                .logout( customizer -> customizer.logoutUrl( "/logout" ).logoutSuccessHandler( logoutSuccessHandler ) )
                .addFilterBefore( authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class );
        return http.build();
    }

    /**
     * Provides a {@link PasswordEncoder} bean for encoding passwords.
     * <p>
     * This bean uses {@link BCryptPasswordEncoder} to securely hash passwords.
     * </p>
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides an {@link AuthenticationManager} bean for handling authentication.
     * <p>
     * This bean is configured using {@link AuthenticationConfiguration} to manage authentication processes.
     * </p>
     */
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config ) throws Exception {
        return config.getAuthenticationManager();
    }
}