/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.oauth2.OAuth2AccessTokenRepoImpl;
import com.pamarin.oauth2.RedisOAuth2AccessTokenRepo;
import com.pamarin.oauth2.RedisOAuth2AuthorizationCodeRepo;
import com.pamarin.oauth2.RedisOAuth2RefreshTokenRepo;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepo;
import com.pamarin.oauth2.repository.OAuth2AuthorizationCodeRepo;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author jitta
 */
@Configuration
@Profile("!test") //inactive for test profile
public class RepoConf {

    @Value("${spring.session.access-token.timeout}")
    private Integer accessTokenTimeout;

    @Value("${spring.session.refresh-token.timeout}")
    private Integer refreshTokenTimeout;

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
