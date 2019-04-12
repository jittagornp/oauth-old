/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import com.pamarin.oauth2.domain.OAuth2Token;
import com.pamarin.oauth2.repository.OAuth2TokenRepositoryAdapter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 * @param <T>
 */
public abstract class RedisOAuth2TokenRepositoryAdapter<T extends OAuth2Token> extends OAuth2TokenRepositoryAdapter<T> {

    private static final Logger LOG = LoggerFactory.getLogger(RedisOAuth2TokenRepositoryAdapter.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    protected abstract String getTokenProfix();
    
    private String makeKey(String tokenId) {
        return getTokenProfix() + ":" + tokenId;
    }

    @Override
    public T doSave(T token) {
        try {
            String key = makeKey(token.getTokenId());
            String value = objectMapper.writeValueAsString(token);
            LOG.debug("Redis set \"{}\" = {}", key, value);
            redisTemplate.opsForValue().set(key, value, getExpiresMinutes(), TimeUnit.MINUTES);
            return token;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Can't parse object to JSON string", ex);
        } 
    }

    @Override
    public T findByTokenId(String tokenId) {
        try {
            String key = makeKey(tokenId);
            String value = redisTemplate.opsForValue().get(key);
            LOG.debug("Redis get \"{}\" = {}", key, value);
            if (value == null) {
                return null;
            }
            return objectMapper.readValue(value, getTokenClass());
        } catch (IOException ex) {
            throw new RuntimeException("Can't parse JSON string to object", ex);
        }
    }

    @Override
    public void deleteByTokenId(String tokenId) {
        redisTemplate.delete(makeKey(tokenId));
    }

}
