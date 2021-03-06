/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository.redis;

import com.pamarin.oauth2.collection.OAuth2RefreshToken;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
public class RedisOAuth2RefreshTokenRepository extends RedisOAuth2TokenRepositoryAdapter<OAuth2RefreshToken> {

    private final int expiresMinutes;

    public RedisOAuth2RefreshTokenRepository(int expiresMinutes) {
        this.expiresMinutes = expiresMinutes;
    }

    @Override
    protected Class<OAuth2RefreshToken> getTokenClass() {
        return OAuth2RefreshToken.class;
    }

    @Override
    protected String getTokenProfix() {
        return "oauth2_refresh_token";
    }

    @Override
    protected int getExpiresMinutes() {
        return expiresMinutes;
    }

}
