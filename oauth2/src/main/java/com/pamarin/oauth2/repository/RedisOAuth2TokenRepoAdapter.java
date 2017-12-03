/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import com.pamarin.oauth2.domain.OAuth2Token;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 * @param <TOKEN>
 */
public abstract class RedisOAuth2TokenRepoAdapter<TOKEN extends OAuth2Token> implements OAuth2TokenRepo<TOKEN> {

    private static final Logger LOG = LoggerFactory.getLogger(RedisOAuth2TokenRepoAdapter.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    protected abstract Class<TOKEN> getTokenClass();

    protected abstract String getTokenProfix();

    protected abstract int getExpiresMinutes();
    
    private String randomId(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public TOKEN save(TOKEN token) {
        try {
            TOKEN clone = (TOKEN) token.clone();
            if (clone.getId() == null) {
                clone.setId(clone.getUserId() + ":" + randomId());
            }

            clone.setExpireMinutes(getExpiresMinutes());
            if (clone.getIssuedAt() < 1) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expires = now.plusMinutes(getExpiresMinutes());
                clone.setIssuedAt(Timestamp.valueOf(now).getTime());
                clone.setExpiresAt(Timestamp.valueOf(expires).getTime());
            }

            String key = getTokenProfix() + clone.getId();
            String value = objectMapper.writeValueAsString(clone);
            LOG.debug("Redis set {} : {}", key, value);
            redisTemplate.opsForValue().set(key, value, getExpiresMinutes(), TimeUnit.MINUTES);
            return clone;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Can't parse object to JSON string", ex);
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException("Can't clone token", ex);
        }
    }

    @Override
    public TOKEN findById(String id) {
        try {
            String key = getTokenProfix() + id;
            String value = redisTemplate.opsForValue().get(key);
            LOG.debug("Redis get {} : {}", key, value);
            if (value == null) {
                return null;
            }
            return objectMapper.readValue(value, getTokenClass());
        } catch (IOException ex) {
            throw new RuntimeException("Can't parse JSON string to object", ex);
        }
    }

}
