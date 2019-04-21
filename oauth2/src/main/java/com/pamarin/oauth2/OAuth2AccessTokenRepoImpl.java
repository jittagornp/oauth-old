/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.repository.redis.RedisOAuth2AccessTokenRepository;
import com.pamarin.oauth2.repository.mongodb.MongodbOAuth2AccessTokenRepository;
import com.pamarin.oauth2.collection.OAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepository;

/**
 *
 * @author jitta
 */
public class OAuth2AccessTokenRepoImpl implements OAuth2AccessTokenRepository {

    @Autowired
    private RedisOAuth2AccessTokenRepository redisOAuth2AccessTokenRepository;

    @Autowired
    private MongodbOAuth2AccessTokenRepository mongodbOAuth2AccessTokenRepository;

    @Override
    public OAuth2AccessToken save(OAuth2AccessToken token) {
        OAuth2AccessToken accessToken = mongodbOAuth2AccessTokenRepository.save(token);
        return redisOAuth2AccessTokenRepository.save(accessToken);
    }

    @Override
    public OAuth2AccessToken findByTokenId(String tokenId) {
        OAuth2AccessToken accessToken = redisOAuth2AccessTokenRepository.findByTokenId(tokenId);
        if (accessToken == null) {
            return mongodbOAuth2AccessTokenRepository.findByTokenId(tokenId);
        }
        return accessToken;
    }

    @Override
    public void deleteByTokenId(String tokenId) {
        redisOAuth2AccessTokenRepository.deleteByTokenId(tokenId);
        mongodbOAuth2AccessTokenRepository.deleteByTokenId(tokenId);
    }
}
