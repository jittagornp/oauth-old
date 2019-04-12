/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.oauth2.OAuth2AccessTokenRepoImpl;
import com.pamarin.oauth2.OAuth2RefreshTokenRepoImpl;
import com.pamarin.oauth2.repository.redis.RedisOAuth2AccessTokenRepository;
import com.pamarin.oauth2.repository.redis.RedisOAuth2AuthorizationCodeRepository;
import com.pamarin.oauth2.repository.redis.RedisOAuth2RefreshTokenRepository;
import com.pamarin.oauth2.repository.mongodb.MongodbOAuth2AccessTokenRepository;
import com.pamarin.oauth2.repository.mongodb.MongodbOAuth2RefreshTokenRepository;
import com.pamarin.oauth2.repository.mongodb.MongodbUserSessionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepository;
import com.pamarin.oauth2.repository.OAuth2AuthorizationCodeRepository;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepository;
import com.pamarin.oauth2.repository.UserSessionRepository;

/**
 *
 * @author jitta
 */
@Configuration
@Profile("!test") //inactive for test profile
public class RepositoryConf {

    @Value("${spring.session.access-token.timeout}")
    private Integer accessTokenTimeout;

    @Value("${spring.session.refresh-token.timeout}")
    private Integer refreshTokenTimeout;

    @Bean
    public OAuth2AuthorizationCodeRepository newOAuth2AuthorizationCodeRepository() {
        return new RedisOAuth2AuthorizationCodeRepository(1);
    }

    @Bean
    public RedisOAuth2AccessTokenRepository newRedisOAuth2AccessTokenRepository() {
        return new RedisOAuth2AccessTokenRepository(accessTokenTimeout / 60);
    }

    @Bean
    public RedisOAuth2RefreshTokenRepository newRedisOAuth2RefreshTokenRepository() {
        return new RedisOAuth2RefreshTokenRepository(refreshTokenTimeout / 60);
    }

    @Bean
    public MongodbOAuth2AccessTokenRepository newMongodbOAuth2AccessTokenRepository() {
        return new MongodbOAuth2AccessTokenRepository(accessTokenTimeout / 60);
    }

    @Bean
    public MongodbOAuth2RefreshTokenRepository newMongodbOAuth2RefreshTokenRepository() {
        return new MongodbOAuth2RefreshTokenRepository(refreshTokenTimeout / 60);
    }

    @Bean
    public OAuth2AccessTokenRepository newOAuth2AccessTokenRepository() {
        return new OAuth2AccessTokenRepoImpl();
    }

    @Bean
    public OAuth2RefreshTokenRepository newOAuth2RefreshTokenRepository() {
        return new OAuth2RefreshTokenRepoImpl();
    }
    
    @Bean
    public UserSessionRepository newMongodbUserSessionRepository(){
        return new MongodbUserSessionRepository();
    }
}
