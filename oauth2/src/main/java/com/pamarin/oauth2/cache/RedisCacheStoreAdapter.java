/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 *
 * @author jitta
 * @param <T>
 */
@Slf4j
public abstract class RedisCacheStoreAdapter<T extends Serializable> implements CacheStore<T> {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    protected abstract String getPrefix();

    protected abstract Class<T> getTypeClass();

    private String makeKey(String key) {
        return getPrefix() + ":" + key;
    }

    @Override
    public void cache(String key, T value) {
        try {
            String fullKey = makeKey(key);
            String json = objectMapper.writeValueAsString(value);
            log.debug("Redis set \"{}\" = {}", fullKey, json);
            redisTemplate.opsForValue().set(fullKey, json, getExpiresMinutes(), TimeUnit.MINUTES);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Can't parse object to JSON string", ex);
        }
    }

    @Override
    public T get(String key) {
        try {
            String fullKey = makeKey(key);
            String value = redisTemplate.opsForValue().get(fullKey);
            log.debug("Redis get \"{}\" = {}", fullKey, value);
            if(value == null){
                return null;
            }
            return objectMapper.readValue(value, getTypeClass());
        } catch (IOException ex) {
            throw new RuntimeException("Can't parse JSON string to object", ex);
        }
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(makeKey(key));
    }

}
