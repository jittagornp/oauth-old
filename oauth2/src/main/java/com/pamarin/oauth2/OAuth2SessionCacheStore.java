/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.model.OAuth2Session;
import org.springframework.stereotype.Service;

/**
 *
 * @author jitta
 */
@Service
public class OAuth2SessionCacheStore extends RedisCacheStoreAdaptor<OAuth2Session> {

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
