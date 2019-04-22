/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import com.pamarin.oauth2.collection.OAuth2AccessToken;
import com.pamarin.oauth2.model.OAuth2Session;

/**
 *
 * @author jitta
 */
public interface OAuth2SessionService {
    
    OAuth2Session getSessionByOAuth2AccessToken(OAuth2AccessToken accessToken);
    
}
