/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository.redis;

import com.pamarin.oauth2.collection.OAuth2AccessToken;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
public class RedisOAuth2AccessTokenRepository extends RedisOAuth2TokenRepositoryAdapter<OAuth2AccessToken> {

    private final int expiresMinutes;

    public RedisOAuth2AccessTokenRepository(int expiresMinutes) {
        this.expiresMinutes = expiresMinutes;
    }

    @Override
    protected Class<OAuth2AccessToken> getTokenClass() {
        return OAuth2AccessToken.class;
    }

    @Override
    protected String getTokenProfix() {
        return "oauth2_access_token";
    }

    @Override
    protected int getExpiresMinutes() {
        return expiresMinutes;
    }

}
