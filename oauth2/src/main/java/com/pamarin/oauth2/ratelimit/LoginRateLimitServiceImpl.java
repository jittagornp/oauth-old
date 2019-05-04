/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.ratelimit;

import com.pamarin.commons.resolver.DefaultHttpClientIPAddressResolver;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 *
 * @author jitta
 */
@Service
public class LoginRateLimitServiceImpl implements LoginRateLimitService {

    private final RateLimitService usernameRateLimitService;

    private final RateLimitService ipAddressRateLimitService;

    private final HttpClientIPAddressResolver ipAddressResolver;

    public LoginRateLimitServiceImpl() {
        this.usernameRateLimitService = new TimesPerSecondRateLimitService(1);
        this.ipAddressRateLimitService = new TimesPerSecondRateLimitService(100);
        this.ipAddressResolver = new DefaultHttpClientIPAddressResolver();
    }

    @Override
    public void limit(String username) {
        this.usernameRateLimitService.limit(username);
    }

    @Override
    public void limit(HttpServletRequest httpReq) {
        String ipAddress = ipAddressResolver.resolve(httpReq);
        ipAddressRateLimitService.limit(ipAddress);
    }

}
