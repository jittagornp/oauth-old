/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.domain.OAuth2RefreshToken;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepo;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
public class RedisOAuth2RefreshTokenRepo extends RedisOAuth2TokenRepoAdapter<OAuth2RefreshToken> implements OAuth2RefreshTokenRepo {

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
        return 60 * 24 * 14;//2 Weeks
    }

}
