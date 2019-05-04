/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.ratelimit;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author jitta
 */
@Service
public class LoginRateLimitServiceImpl implements LoginRateLimitService {

    private final RateLimitService usernameRateLimitService;

    private final HttpRequestRateLimitService httpRequestRateLimitService;

    public LoginRateLimitServiceImpl(@Qualifier("loginTokenBucketRepository") TokenBucketRepository tokenBucketRepository) {
        this.usernameRateLimitService = new TimesPerSecondRateLimitService(1, tokenBucketRepository);
        this.httpRequestRateLimitService = new DefaultHttpRequestRateLimitService(tokenBucketRepository);
    }

    @Override
    public void limit(String username) {
        this.usernameRateLimitService.limit(username);
    }

    @Override
    public void limit(HttpServletRequest httpReq) {
        this.httpRequestRateLimitService.limit(httpReq);
    }

}
