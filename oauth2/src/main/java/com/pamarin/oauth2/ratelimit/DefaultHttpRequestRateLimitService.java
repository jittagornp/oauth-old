/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.ratelimit;

import com.pamarin.commons.resolver.DefaultHttpClientIPAddressResolver;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jitta
 */
public class DefaultHttpRequestRateLimitService implements HttpRequestRateLimitService {

    private final RateLimitService ipAddressRateLimitService;

    private final RateLimitService sessionRateLimitService;

    private final HttpClientIPAddressResolver ipAddressResolver;

    public DefaultHttpRequestRateLimitService(String serviceName) {
        this.ipAddressRateLimitService = new TimesPerSecondRateLimitService(10);
        this.sessionRateLimitService = new TimesPerSecondRateLimitService(5);
        this.ipAddressResolver = new DefaultHttpClientIPAddressResolver();
    }

    @Override
    public void limit(HttpServletRequest httpReq) {
        HttpSession session = httpReq.getSession(false);
        if (session == null) {
            String ipAddress = ipAddressResolver.resolve(httpReq);
            ipAddressRateLimitService.limit(ipAddress);
        } else {
            sessionRateLimitService.limit(session.getId());
        }
    }
}
