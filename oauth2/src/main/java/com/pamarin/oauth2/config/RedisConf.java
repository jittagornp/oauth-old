/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.config;

import java.lang.reflect.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.util.ReflectionUtils;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Configuration
@EnableRedisHttpSession
@Order(Ordered.HIGHEST_PRECEDENCE)
@Profile("!test") //inactive for test profile
public class RedisConf {

    @Value("${spring.session.timeout}")
    private Integer maxInactiveIntervalInSeconds;

    @Value("${spring.session.redis.namespace}")
    private String namespace;

    @Value("${spring.session.redis.flush-mode}")
    private String flushMode;

    @Bean
    public RedisOperationsSessionRepository sessionRepository(RedisConnectionFactory factory) {
        RedisOperationsSessionRepository sessionRepository = new RedisOperationsSessionRepository(factory);

        sessionRepository.cleanupExpiredSessions();
        
        Field keyPrefixField = ReflectionUtils.findField(RedisOperationsSessionRepository.class, "keyPrefix");
        keyPrefixField.setAccessible(true);
        ReflectionUtils.setField(keyPrefixField, sessionRepository, namespace + ":");
        
        sessionRepository.setDefaultMaxInactiveInterval(maxInactiveIntervalInSeconds);
        sessionRepository.setRedisFlushMode("on-save".equals(flushMode) ? RedisFlushMode.ON_SAVE : RedisFlushMode.IMMEDIATE);
        return sessionRepository;
    }

}
