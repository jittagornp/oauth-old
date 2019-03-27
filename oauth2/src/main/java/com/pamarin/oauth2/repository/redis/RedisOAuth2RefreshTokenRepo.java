/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository.redis;

import com.pamarin.oauth2.domain.OAuth2RefreshToken;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
public class RedisOAuth2RefreshTokenRepo extends RedisOAuth2TokenRepoAdapter<OAuth2RefreshToken> {

    private final int expiresMinutes;

    public RedisOAuth2RefreshTokenRepo(int expiresMinutes) {
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
