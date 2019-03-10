/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.domain.OAuth2AuthorizationCode;
import com.pamarin.oauth2.repository.OAuth2AuthorizationCodeRepo;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
public class RedisOAuth2AuthorizationCodeRepo extends RedisOAuth2TokenRepoAdapter<OAuth2AuthorizationCode> implements OAuth2AuthorizationCodeRepo {

    private final int expiresMinutes;

    public RedisOAuth2AuthorizationCodeRepo(int expiresMinutes) {
        this.expiresMinutes = expiresMinutes;
    }

    @Override
    protected Class<OAuth2AuthorizationCode> getTokenClass() {
        return OAuth2AuthorizationCode.class;
    }

    @Override
    protected String getTokenProfix() {
        return "oauth2_code";
    }

    @Override
    protected int getExpiresMinutes() {
        return expiresMinutes;
    }

}
