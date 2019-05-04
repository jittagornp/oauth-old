/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.oauth2.ratelimit.InMemoryTokenBucketRepository;
import com.pamarin.oauth2.ratelimit.TokenBucketRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author jitta
 */
@Configuration
public class RateLimitConfig {

    @Bean("defaultTokenBucketRepository")
    public TokenBucketRepository defaultTokenBucketRepository() {
        return new InMemoryTokenBucketRepository();
    }

    @Bean("loginTokenBucketRepository")
    public TokenBucketRepository loginTokenBucketRepository() {
        return new InMemoryTokenBucketRepository();
    }
}
