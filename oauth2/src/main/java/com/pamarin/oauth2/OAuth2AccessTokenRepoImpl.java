/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.repository.MongodbOAuth2AccessTokenRepo;
import com.pamarin.oauth2.domain.OAuth2AccessToken;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jitta
 */
public class OAuth2AccessTokenRepoImpl implements OAuth2AccessTokenRepo {

    private final RedisOAuth2AccessTokenRepo redisOAuth2AccessTokenRepo;

    @Autowired
    private MongodbOAuth2AccessTokenRepo mongodbOAuth2AccessTokenRepo;

    public OAuth2AccessTokenRepoImpl(RedisOAuth2AccessTokenRepo redisOAuth2AccessTokenRepo) {
        this.redisOAuth2AccessTokenRepo = redisOAuth2AccessTokenRepo;
    }

    @Override
    public OAuth2AccessToken save(OAuth2AccessToken token) {
        OAuth2AccessToken accessToken = redisOAuth2AccessTokenRepo.save(token);
        mongodbOAuth2AccessTokenRepo.save(accessToken);
        return accessToken;
    }

    @Override
    public OAuth2AccessToken findByTokenId(String id) {
        return redisOAuth2AccessTokenRepo.findByTokenId(id);
    }

    @Override
    public void deleteByTokenId(String id) {
        redisOAuth2AccessTokenRepo.deleteByTokenId(id);
        mongodbOAuth2AccessTokenRepo.delete(id);
    }

}
