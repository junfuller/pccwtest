package org.example.pccwtest.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class CustomSecurityConfig {

    private final PermitAllUrlProperties       permitAllUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf( AbstractHttpConfigurer::disable )
                .authorizeHttpRequests(
                        customizer -> {
                            //permit all endpoint with @Anonymous
                            permitAllUrl.getUrls().forEach( url -> customizer.requestMatchers( url ).permitAll() );

                            // Permit Swagger API documentation
                            customizer.requestMatchers("/swagger-ui/**").permitAll();
                            customizer.requestMatchers("/v3/api-docs/**").permitAll();
                            customizer.requestMatchers("/swagger-resources/**").permitAll();
                            customizer.requestMatchers("/webjars/**").permitAll();
                        }
                );
        return http.build();
    }
}