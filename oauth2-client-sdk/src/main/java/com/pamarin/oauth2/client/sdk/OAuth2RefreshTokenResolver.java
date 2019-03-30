/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jitta
 */
public interface OAuth2RefreshTokenResolver {

    String resolve(HttpServletRequest httpReq);
    
    String getTokenName();

}
