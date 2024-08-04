package org.example.pccwtest.config;

import org.example.pccwtest.model.LoginUser;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

/**
 * Configuration class for caching.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Defines a {@link Cache} bean for storing authentication tokens.
     * The cache is configured with the following settings:
     * <ul>
     *     <li>Entries expire 2 minutes after the last write or access.</li>
     *     <li>Initial cache size is set to 100 entries.</li>
     *     <li>The maximum number of cache entries is set to {@link Integer#MAX_VALUE}.</li>
     * </ul>
     *
     * @return a {@link Cache} instance for storing {@link String} tokens and {@link LoginUser} objects
     */
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
