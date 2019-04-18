/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jitta
 */
public interface OAuth2LoginSession {

    void login(String accessToken, HttpServletRequest httpReq);

    void logout(HttpServletRequest httpReq);

}
