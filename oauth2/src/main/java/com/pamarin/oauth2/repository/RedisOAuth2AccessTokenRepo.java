/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.OAuth2AccessToken;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
public class RedisOAuth2AccessTokenRepo extends RedisOAuth2TokenRepo<OAuth2AccessToken> implements OAuth2AccessTokenRepo {

    @Override
    protected Class<OAuth2AccessToken> getTokenClass() {
        return OAuth2AccessToken.class;
    }

    @Override
    protected String getTokenProfix() {
        return "oauth2_access_token:";
    }

    @Override
    protected int getExpiresMinutes() {
        return 30;
    }

}
