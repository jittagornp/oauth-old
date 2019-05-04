/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.ratelimit;

import io.github.bucket4j.Bucket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jitta
 */
public class InMemoryTokenBucketRepository implements TokenBucketRepository {

    private final Map<String, Bucket> cached;

    public InMemoryTokenBucketRepository() {
        this.cached = new HashMap<>();
    }

    @Override
    public Bucket find(String key) {
        return cached.get(key);
    }

    @Override
    public void save(String key, Bucket bucket) {
        cached.put(key, bucket);
    }

    @Override
    public void delete(String key) {
        cached.remove(key);
    }

}
