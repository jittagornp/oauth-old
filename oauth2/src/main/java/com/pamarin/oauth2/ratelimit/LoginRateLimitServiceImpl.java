/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.ratelimit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author jitta
 */
@Service
public class LoginRateLimitServiceImpl implements LoginRateLimitService {

    private final RateLimitService usernameRateLimitService;

    @Autowired
    public LoginRateLimitServiceImpl(@Qualifier("loginTokenBucketRepository") TokenBucketRepository tokenBucketRepository) {
        this.usernameRateLimitService = new TimesPerSecondRateLimitService(1, tokenBucketRepository);
    }

    @Override
    public void limit(String username) {
        this.usernameRateLimitService.limit(username);
    }


}
