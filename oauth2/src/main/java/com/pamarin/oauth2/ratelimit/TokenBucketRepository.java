/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.ratelimit;

import io.github.bucket4j.Bucket;

/**
 *
 * @author jitta
 */
public interface TokenBucketRepository {

    Bucket find(String key);

    void save(String key, Bucket bucket);

    void delete(String key);

    void delete();
}
