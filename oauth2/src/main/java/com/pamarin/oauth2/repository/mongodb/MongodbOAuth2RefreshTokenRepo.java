/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository.mongodb;

import com.pamarin.oauth2.collection.OAuth2RefreshToken;

/**
 *
 * @author jitta
 */
public class MongodbOAuth2RefreshTokenRepo extends MongodbOAuth2TokenRepoAdapter<OAuth2RefreshToken> {

    private final int expiresMinutes;

    public MongodbOAuth2RefreshTokenRepo(int expiresMinutes) {
        this.expiresMinutes = expiresMinutes;
    }

    @Override
    protected Class<OAuth2RefreshToken> getTokenClass() {
        return OAuth2RefreshToken.class;
    }

    @Override
    protected int getExpiresMinutes() {
        return expiresMinutes;
    }

}
