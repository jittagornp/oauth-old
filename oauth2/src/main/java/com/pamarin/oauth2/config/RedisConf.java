/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.oauth2.OAuth2AccessTokenRepoImpl;
import com.pamarin.oauth2.RedisOAuth2AccessTokenRepo;
import com.pamarin.oauth2.RedisOAuth2AuthorizationCodeRepo;
import com.pamarin.oauth2.RedisOAuth2RefreshTokenRepo;
import com.pamarin.oauth2.RedisSessionRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.SpringHttpSessionConfiguration;
import org.springframework.session.data.redis.RedisFlushMode;
import com.pamarin.oauth2.repository.DatabaseSessionRepo;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepo;
import com.pamarin.oauth2.repository.OAuth2AuthorizationCodeRepo;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepo;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Configuration
@Profile("!test") //inactive for test profile
public class RedisConf extends SpringHttpSessionConfiguration {

    @Value("${spring.session.timeout}")
    private Integer sessionTimeout;

    @Value("${spring.session.redis.namespace}")
    private String namespace;

    @Value("${spring.session.redis.flush-mode}")
    private String flushMode;

    @Autowired
    private DatabaseSessionRepo databaseSessionRepo;

    @Value("${spring.session.access-token.timeout}")
    private Integer accessTokenTimeout;

    @Value("${spring.session.refresh-token.timeout}")
    private Integer refreshTokenTimeout;

    @Bean
    public SessionRepository sessionRepository(RedisConnectionFactory factory) {
        RedisSessionRepositoryImpl sessionRepository = new RedisSessionRepositoryImpl(factory);
        sessionRepository.setRedisKeyNamespace(namespace);
        sessionRepository.setDefaultMaxInactiveInterval(sessionTimeout);
        sessionRepository.setRedisFlushMode("on-save".equals(flushMode) ? RedisFlushMode.ON_SAVE : RedisFlushMode.IMMEDIATE);
        sessionRepository.setDatabaseSessionRepository(databaseSessionRepo);
        return sessionRepository;
    }

    @Bean
    public OAuth2AuthorizationCodeRepo newOAuth2AuthorizationCodeRepo() {
        return new RedisOAuth2AuthorizationCodeRepo(1);
    }

    @Bean
    public RedisOAuth2AccessTokenRepo newRedisOAuth2AccessTokenRepo() {
        return new RedisOAuth2AccessTokenRepo(accessTokenTimeout / 60);
    }

    @Bean
    public OAuth2RefreshTokenRepo newOAuth2RefreshTokenRepo() {
        return new RedisOAuth2RefreshTokenRepo(refreshTokenTimeout / 60);
    }

    @Bean
    public OAuth2AccessTokenRepo newOAuth2AccessTokenRepo() {
        return new OAuth2AccessTokenRepoImpl();
    }
}
