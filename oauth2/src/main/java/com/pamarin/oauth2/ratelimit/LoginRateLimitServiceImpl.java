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
public class LoginRateLimitServiceImpl implements LoginRateLimitService {

    private static final String SERVICE_NAME = "login";

    private final RateLimitService usernameRateLimitService;

    private final HttpRequestRateLimitService httpRequestRateLimitService;

    public LoginRateLimitServiceImpl() {
        this.usernameRateLimitService = new TimesPerSecondRateLimitService(1);
        this.httpRequestRateLimitService = new DefaultHttpRequestRateLimitService(SERVICE_NAME);
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
