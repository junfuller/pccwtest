package org.example.pccwtest.config;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.example.pccwtest.security.annotation.Anonymous;
import org.example.pccwtest.security.filter.JwtAuthenticationFilter;
import org.example.pccwtest.security.handler.AuthenticationEntryPointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
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

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class CustomSecurityConfig {
    @Resource
    private final  RequestMappingHandlerMapping requestMappingHandlerMapping;

    private final AuthenticationEntryPointImpl unauthorizedHandler;

    private final JwtAuthenticationFilter authenticationTokenFilter;

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
                ).exceptionHandling( configurer -> configurer.authenticationEntryPoint( unauthorizedHandler ) )
                .addFilterBefore( authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}