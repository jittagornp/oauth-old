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

    private int ipAddressTimesPerSecond = 1;

    private int sessionTimesPerSecond = 1;

    public DefaultHttpRequestRateLimitService(TokenBucketRepository tokenBucketRepository) {
        this.ipAddressRateLimitService = new TimesPerSecondRateLimitService(ipAddressTimesPerSecond, tokenBucketRepository);
        this.sessionRateLimitService = new TimesPerSecondRateLimitService(sessionTimesPerSecond, tokenBucketRepository);
        this.ipAddressResolver = new DefaultHttpClientIPAddressResolver();
    }

    public void setIpAddressTimesPerSecond(int ipAddressTimesPerSecond) {
        this.ipAddressTimesPerSecond = ipAddressTimesPerSecond;
    }

    public void setSessionTimesPerSecond(int sessionTimesPerSecond) {
        this.sessionTimesPerSecond = sessionTimesPerSecond;
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
