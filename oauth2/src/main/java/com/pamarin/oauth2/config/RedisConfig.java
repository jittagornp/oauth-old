/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.oauth2.RedisSessionRepositoryImpl;
import com.pamarin.oauth2.RevokeTokenServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.SpringHttpSessionConfiguration;
import org.springframework.session.data.redis.RedisFlushMode;
import com.pamarin.oauth2.service.RevokeTokenService;
import com.pamarin.oauth2.repository.DatabaseSessionRepository;
import com.pamarin.oauth2.repository.redis.RedisOAuth2AccessTokenRepository;
import com.pamarin.oauth2.repository.redis.RedisOAuth2RefreshTokenRepository;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Configuration
@Profile("!test") //inactive for test profile
public class RedisConfig extends SpringHttpSessionConfiguration {

    @Value("${spring.session.timeout}")
    private Integer sessionTimeout;

    @Value("${spring.session.redis.namespace}")
    private String namespace;

    @Value("${spring.session.redis.flush-mode}")
    private String flushMode;

    @Bean
    public SessionRepository sessionRepository(RedisConnectionFactory factory, DatabaseSessionRepository databaseSessionRepository) {
        RedisSessionRepositoryImpl sessionRepository = new RedisSessionRepositoryImpl(factory);
        sessionRepository.setRedisKeyNamespace(namespace);
        sessionRepository.setDefaultMaxInactiveInterval(sessionTimeout);
        sessionRepository.setRedisFlushMode("on-save".equals(flushMode) ? RedisFlushMode.ON_SAVE : RedisFlushMode.IMMEDIATE);
        sessionRepository.setDatabaseSessionRepository(databaseSessionRepository);
        return sessionRepository;
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
