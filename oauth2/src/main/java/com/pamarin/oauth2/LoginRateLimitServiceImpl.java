/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.exception.LoginOverLimitException;
import com.pamarin.oauth2.service.LoginRateLimitService;
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

    private final Map<String, Bucket> cached;

    public LoginRateLimitServiceImpl() {
        this.cached = new HashMap<>();
    }

    @Override
    public void checkLimit(String username) {
        Bucket bucket = cached.get(username);
        if (bucket == null) {
            // define the limit 1 time per 1 minute
            Bandwidth limit = Bandwidth.simple(1, Duration.ofSeconds(1));
            // construct the bucket
            bucket = Bucket4j.builder().addLimit(limit).build();
            cached.put(username, bucket);
        }

        if (!bucket.tryConsume(1)) {
            throw new LoginOverLimitException("username \"" + username + "\" is login over limit.");
        }
    }

}
