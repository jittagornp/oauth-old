/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.ratelimit;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 *
 * @author jitta
 */
@Service
public class AuthorizeRateLimitServiceImpl implements AuthorizeRateLimitService {

    private static final String SERVICE_NAME = "authorize";

    private final HttpRequestRateLimitService httpRequestRateLimitService;

    public AuthorizeRateLimitServiceImpl() {
        this.httpRequestRateLimitService = new DefaultHttpRequestRateLimitService(SERVICE_NAME);
    }

    @Override
    public void limit(HttpServletRequest httpReq) {
        httpRequestRateLimitService.limit(httpReq);
    }

}
