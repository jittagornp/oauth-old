/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.ratelimit;

import io.github.bucket4j.Bucket;

/**
 *
 * @author jitta
 */
public interface RateLimitService {

    TokenBucketRepository getTokenBucketRepository();

    Bucket createTokenBucket();

    void limit(String key);

}
