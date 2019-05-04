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
public class AuthorizeRateLimitServiceImpl implements AuthorizeRateLimitService {

    private final RateLimitService rateLimitService;

    private final HttpClientIPAddressResolver ipAddressResolver;

    public AuthorizeRateLimitServiceImpl() {
        this.rateLimitService = new TimesPerSecondRateLimitService(100);
        this.ipAddressResolver = new DefaultHttpClientIPAddressResolver();
    }

    @Override
    public void limit(HttpServletRequest httpReq) {
        String ipAddress = ipAddressResolver.resolve(httpReq);
        rateLimitService.limit(ipAddress);
    }

}
