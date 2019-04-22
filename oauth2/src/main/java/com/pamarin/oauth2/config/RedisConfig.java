/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.oauth2.RevokeTokenServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.SpringHttpSessionConfiguration;
import com.pamarin.oauth2.service.RevokeTokenService;
import com.pamarin.oauth2.repository.redis.RedisOAuth2AccessTokenRepository;
import com.pamarin.oauth2.repository.redis.RedisOAuth2RefreshTokenRepository;
import com.pamarin.oauth2.resolver.UserAgentTokenIdResolver;
import com.pamarin.oauth2.session.CustomSessionRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Configuration
@Profile("!test") //inactive for test profile
public class RedisConfig extends SpringHttpSessionConfiguration {

    @Value("${spring.session.timeout:#{1800}}")
    private int sessionTimeout;

    @Value("${spring.session.redis.namespace}")
    private String namespace;

    @Bean
    public SessionRepository newSessionRepository(
            RedisConnectionFactory factory,
            MongoOperations mongoOperations,
            UserAgentTokenIdResolver userAgentTokenIdResolver
    ) {
        CustomSessionRepository repository = new CustomSessionRepository(
                createDefaultTemplate(factory),
                mongoOperations,
                userAgentTokenIdResolver
        );
        repository.setMaxInactiveIntervalInSeconds(sessionTimeout);
        repository.setSessionNameSpace(namespace);
        repository.setSynchronizeTimeout(1000 * 30); //30 seconds
        return repository;
    }

    private static RedisTemplate<Object, Object> createDefaultTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RevokeTokenService newRevokeTokenServiceImpl(
            RedisOAuth2AccessTokenRepository redisOAuth2AccessTokenRepository,
            RedisOAuth2RefreshTokenRepository redisOAuth2RefreshTokenRepository,
            MongoOperations mongoOperations
    ) {
        return new RevokeTokenServiceImpl(
                redisOAuth2AccessTokenRepository,
                redisOAuth2RefreshTokenRepository,
                mongoOperations
        );
    }
}
