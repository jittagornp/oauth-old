/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository.mongodb;

import com.pamarin.oauth2.collection.OAuth2AccessToken;

/**
 *
 * @author jitta
 */
public class MongodbOAuth2AccessTokenRepository extends MongodbOAuth2TokenRepositoryAdapter<OAuth2AccessToken> {

    private final int expiresMinutes;

    public MongodbOAuth2AccessTokenRepository(int expiresMinutes) {
        this.expiresMinutes = expiresMinutes;
    }

    @Override
    protected Class<OAuth2AccessToken> getTokenClass() {
        return OAuth2AccessToken.class;
    }

    @Override
    protected int getExpiresMinutes() {
        return expiresMinutes;
    }

}
