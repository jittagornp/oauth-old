/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pamarin.oauth2.domain.OAuth2RefreshToken;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepo;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Repository
public class RedisOAuth2RefreshTokenRepoImpl implements OAuth2RefreshTokenRepo {

    private static final Logger LOG = LoggerFactory.getLogger(RedisOAuth2RefreshTokenRepoImpl.class);

    private static final String TOKEN_PREFIX = "oauth2_refresh_token:";

    private static final int EXPIRES_MINUTE = 60 * 8; // 8 hr

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Override
    public OAuth2RefreshToken save(OAuth2RefreshToken refreshToken) {
        try {
            OAuth2RefreshToken clone = (OAuth2RefreshToken) refreshToken.clone();
            if (clone.getId() == null) {
                clone.setId(UUID.randomUUID().toString().replace("-", ""));
            }
            String key = TOKEN_PREFIX + clone.getId();
            String value = jacksonObjectMapper.writeValueAsString(clone);
            LOG.debug("Redis set {} : {}", key, value);
            redisTemplate.opsForValue().set(key, value, EXPIRES_MINUTE, TimeUnit.MINUTES);
            return clone;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Can't parse object to JSON string", ex);
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException("Can't clone refresh token", ex);
        }
    }

    @Override
    public OAuth2RefreshToken findById(String id) {
        try {
            String key = TOKEN_PREFIX + id;
            String value = redisTemplate.opsForValue().get(key);
            LOG.debug("Redis get {} : {}", key, value);
            if(value == null){
                return null;
            }
            return jacksonObjectMapper.readValue(value, OAuth2RefreshToken.class);
        } catch (IOException ex) {
            throw new RuntimeException("Can't parse JSON string to object", ex);
        }
    }

}
