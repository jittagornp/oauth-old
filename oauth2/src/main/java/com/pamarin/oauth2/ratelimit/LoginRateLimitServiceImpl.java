/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.ratelimit;

import com.pamarin.oauth2.exception.RateLimitException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author jitta
 */
@Service
public class LoginRateLimitServiceImpl implements LoginRateLimitService {

    private static final int TIMES_PER_SECOND = 1;

    private final Map<String, Bucket> cached;

    public LoginRateLimitServiceImpl() {
        this.cached = new HashMap<>();
    }

    private Bucket createBucket() {
        // define the limit 1 time per 1 second
        Bandwidth limit = Bandwidth.simple(TIMES_PER_SECOND, Duration.ofSeconds(1));
        // construct the bucket
        return Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    public void limit(String username) {
        Bucket bucket = cached.get(username);
        if (bucket == null) {
            bucket = createBucket();
            cached.put(username, bucket);
        }

        if (!bucket.tryConsume(1)) {
            throw new RateLimitException("username \"" + username + "\" is over limit on /login.");
        }
    }

}
