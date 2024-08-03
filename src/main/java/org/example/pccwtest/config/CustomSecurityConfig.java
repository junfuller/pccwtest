package org.example.pccwtest.config;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.example.pccwtest.security.Anonymous;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
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
                            customizer.requestMatchers( "/swagger-ui/**", "/v3/api-docs/**", "/*.html", "/*.ico" ).permitAll();
                        }
                );
        return http.build();
    }
}