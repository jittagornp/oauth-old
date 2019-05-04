/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.ratelimit;

import com.pamarin.commons.resolver.DefaultHttpClientIPAddressResolver;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import com.pamarin.oauth2.exception.RateLimitException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 *
 * @author jitta
 */
@Service
public class AuthorizeRateLimitServiceImpl implements AuthorizeRateLimitService {

    private static final int TIMES_PER_SECOND = 1;
    
    private final Map<String, Bucket> cached;

    private final HttpClientIPAddressResolver ipAddressResolver;

    public AuthorizeRateLimitServiceImpl() {
        this.cached = new HashMap<>();
        this.ipAddressResolver = new DefaultHttpClientIPAddressResolver();
    }

    private Bucket createBucket() {
        // define the limit 10 time per 1 second
        Bandwidth limit = Bandwidth.simple(TIMES_PER_SECOND, Duration.ofSeconds(1));
        // construct the bucket
        return Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    public void limit(HttpServletRequest httpReq) {
        String ipAddress = ipAddressResolver.resolve(httpReq);
        Bucket bucket = cached.get(ipAddress);
        if (bucket == null) {
            bucket = createBucket();
            cached.put(ipAddress, bucket);
        }

        if (!bucket.tryConsume(1)) {
            throw new RateLimitException("ip address \"" + ipAddress + "\" is over limit on /authorize.");
        }
    }

}
