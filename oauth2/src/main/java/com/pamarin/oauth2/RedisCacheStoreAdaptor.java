/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import java.util.concurrent.TimeUnit;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 *
 * @author jitta
 * @param <T>
 */
public abstract class RedisCacheStoreAdaptor<T> implements CacheStore<T> {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(RedisCacheStoreAdaptor.class);

    @Autowired
    private RedisTemplate<String, T> redisTemplate;

    protected abstract String getPrefix();

    private String makeKey(String key) {
        return getPrefix() + ":" + key;
    }

    @Override
    public void cache(String key, T value) {
        String fullKey = makeKey(key);
        LOG.debug("Redis set \"{}\" = {}", key, value);
        redisTemplate.opsForValue().set(fullKey, value, getExpiresMinutes(), TimeUnit.MINUTES);
    }

    @Override
    public T get(String key) {
        String fullKey = makeKey(key);
        T value = redisTemplate.opsForValue().get(fullKey);
        LOG.debug("Redis get \"{}\" = {}", key, value);
        return value;
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(makeKey(key));
    }

}
