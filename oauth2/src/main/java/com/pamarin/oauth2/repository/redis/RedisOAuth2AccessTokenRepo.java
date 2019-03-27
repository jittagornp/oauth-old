/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository.redis;

import com.pamarin.oauth2.domain.OAuth2AccessToken;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
public class RedisOAuth2AccessTokenRepo extends RedisOAuth2TokenRepoAdapter<OAuth2AccessToken> {

    private final int expiresMinutes;

    public RedisOAuth2AccessTokenRepo(int expiresMinutes) {
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
