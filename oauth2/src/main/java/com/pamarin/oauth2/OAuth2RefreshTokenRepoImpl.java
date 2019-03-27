/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.repository.redis.RedisOAuth2RefreshTokenRepo;
import com.pamarin.oauth2.domain.OAuth2RefreshToken;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepo;
import com.pamarin.oauth2.repository.mongodb.MongodbOAuth2RefreshTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jitta
 */
public class OAuth2RefreshTokenRepoImpl implements OAuth2RefreshTokenRepo {

    @Autowired
    private RedisOAuth2RefreshTokenRepo redisOAuth2RefreshTokenRepo;

    @Autowired
    private MongodbOAuth2RefreshTokenRepo mongodbOAuth2RefreshTokenRepo;

    @Override
    public OAuth2RefreshToken save(OAuth2RefreshToken token) {
        return mongodbOAuth2RefreshTokenRepo.save(redisOAuth2RefreshTokenRepo.save(token));
    }

    @Override
    public OAuth2RefreshToken findByTokenId(String tokenId) {
        return redisOAuth2RefreshTokenRepo.findByTokenId(tokenId);
    }

    @Override
    public void deleteByTokenId(String tokenId) {
        redisOAuth2RefreshTokenRepo.deleteByTokenId(tokenId);
        mongodbOAuth2RefreshTokenRepo.deleteByTokenId(tokenId);
    }

}
