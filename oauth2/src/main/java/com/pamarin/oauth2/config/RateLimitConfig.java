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

    @Bean("loginTokenBucketRepository")
    public TokenBucketRepository newLoginTokenBucketRepository() {
        return new InMemoryTokenBucketRepository();
    }

    @Bean("authorizeTokenBucketRepository")
    public TokenBucketRepository newAuthorizeTokenBucketRepository() {
        return new InMemoryTokenBucketRepository();
    }
}
