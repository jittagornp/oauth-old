/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.repository.redis.RedisOAuth2AccessTokenRepo;
import com.pamarin.oauth2.repository.mongodb.MongodbOAuth2AccessTokenRepo;
import com.pamarin.oauth2.collection.OAuth2AccessToken;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jitta
 */
public class OAuth2AccessTokenRepoImpl implements OAuth2AccessTokenRepo {

    @Autowired
    private RedisOAuth2AccessTokenRepo redisOAuth2AccessTokenRepo;

    @Autowired
    private MongodbOAuth2AccessTokenRepo mongodbOAuth2AccessTokenRepo;

    @Override
    public OAuth2AccessToken save(OAuth2AccessToken token) {
        return redisOAuth2AccessTokenRepo.save(mongodbOAuth2AccessTokenRepo.save(token));
    }

    @Override
    public OAuth2AccessToken findByTokenId(String tokenId) {
        return redisOAuth2AccessTokenRepo.findByTokenId(tokenId);
    }

    @Override
    public void deleteByTokenId(String tokenId) {
        redisOAuth2AccessTokenRepo.deleteByTokenId(tokenId);
        mongodbOAuth2AccessTokenRepo.deleteByTokenId(tokenId);
    }

}
