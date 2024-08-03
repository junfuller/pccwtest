package org.example.pccwtest.config;

import org.example.pccwtest.model.LoginUser;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean( name = "tokenCache" )
    public Cache<String, LoginUser> tokenCache() {
        return Caffeine.newBuilder()
                // Set the time to expire after the last write or access
                .expireAfterWrite( Duration.ofMinutes( 2 ) )
                // Initial cache size
                .initialCapacity( 100 )
                // Maximum number of cache entries
                .maximumSize( Integer.MAX_VALUE )
                .build();
    }
}
