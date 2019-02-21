/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.cache.OAuth2SessionCacheStore;
import com.pamarin.oauth2.model.OAuth2Session;

/**
 *
 * @author jitta
 */
public class RedisOAuth2SessionCacheStore extends RedisCacheStoreAdaptor<OAuth2Session> implements OAuth2SessionCacheStore {

    @Override
    protected String getPrefix() {
        return "oauth2_session";
    }

    @Override
    public int getExpiresMinutes() {
        return 3;
    }

    @Override
    protected Class<OAuth2Session> getTypeClass() {
        return OAuth2Session.class;
    }

}
