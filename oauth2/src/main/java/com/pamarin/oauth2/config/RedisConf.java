/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.oauth2.RedisUserSessionRepository;
import java.lang.reflect.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.SpringHttpSessionConfiguration;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.util.ReflectionUtils;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Configuration
//@EnableRedisHttpSession
//@Order(Ordered.HIGHEST_PRECEDENCE)
@Profile("!test") //inactive for test profile
public class RedisConf extends SpringHttpSessionConfiguration {

    @Value("${spring.session.timeout}")
    private Integer sessionTimeout;

    @Value("${spring.session.redis.namespace}")
    private String namespace;

    @Value("${spring.session.redis.flush-mode}")
    private String flushMode;

//    @Bean
//    public RedisTemplate<Object, Object> sessionRedisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
////        if (this.defaultRedisSerializer != null) {
////            template.setDefaultSerializer(this.defaultRedisSerializer);
////        }
//        template.setConnectionFactory(connectionFactory);
//        return template;
//    }

    @Bean
    public SessionRepository sessionRepository(RedisConnectionFactory factory) {
        RedisUserSessionRepository sessionRepository = new RedisUserSessionRepository(factory);

        //sessionRepository.cleanupExpiredSessions();

        Field keyPrefixField = ReflectionUtils.findField(RedisUserSessionRepository.class, "keyPrefix");
        keyPrefixField.setAccessible(true);
        ReflectionUtils.setField(keyPrefixField, sessionRepository, namespace + ":");

        sessionRepository.setDefaultMaxInactiveInterval(sessionTimeout);
        sessionRepository.setRedisFlushMode("on-save".equals(flushMode) ? RedisFlushMode.ON_SAVE : RedisFlushMode.IMMEDIATE);
        return sessionRepository;
    }
}
