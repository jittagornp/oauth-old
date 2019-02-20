/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.model.OAuth2Session;

/**
 *
 * @author jitta
 */
public interface OAuth2SessionService {
    
    OAuth2Session getSession(String accessToken);
    
}
