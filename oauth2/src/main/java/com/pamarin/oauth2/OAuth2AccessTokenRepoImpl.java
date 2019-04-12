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
        return redisOAuth2AccessTokenRepository.save(mongodbOAuth2AccessTokenRepository.save(token));
    }

    @Override
    public OAuth2AccessToken findByTokenId(String tokenId) {
        return redisOAuth2AccessTokenRepository.findByTokenId(tokenId);
    }

    @Override
    public void deleteByTokenId(String tokenId) {
        redisOAuth2AccessTokenRepository.deleteByTokenId(tokenId);
        mongodbOAuth2AccessTokenRepository.deleteByTokenId(tokenId);
    }

}
