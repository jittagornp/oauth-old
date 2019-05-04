/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.ratelimit;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author jitta
 */
@Service
public class AuthorizeRateLimitServiceImpl implements AuthorizeRateLimitService {

    private final HttpRequestRateLimitService httpRequestRateLimitService;

    @Autowired
    public AuthorizeRateLimitServiceImpl(@Qualifier("authorizeTokenBucketRepository") TokenBucketRepository tokenBucketRepository) {
        this.httpRequestRateLimitService = new DefaultHttpRequestRateLimitService(tokenBucketRepository);
    }

    @Override
    public void limit(HttpServletRequest httpReq) {
        httpRequestRateLimitService.limit(httpReq);
    }

}
