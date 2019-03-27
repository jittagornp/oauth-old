/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository.mongodb;

import com.pamarin.oauth2.domain.OAuth2AccessToken;

/**
 *
 * @author jitta
 */
public class MongodbOAuth2AccessTokenRepo extends MongodbOAuth2TokenRepoAdapter<OAuth2AccessToken>{

    @Override
    protected Class<OAuth2AccessToken> getTokenClass() {
        return OAuth2AccessToken.class;
    }
    
}
