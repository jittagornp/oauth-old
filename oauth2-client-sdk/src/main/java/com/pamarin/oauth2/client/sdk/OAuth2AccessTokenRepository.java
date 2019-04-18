/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jitta
 */
public interface OAuth2AccessTokenRepository {

    OAuth2AccessToken getAccessTokenByAuthenticationCode(String code, HttpServletRequest httpReq, HttpServletResponse httpResp);

    OAuth2AccessToken getAccessTokenByRefreshToken(String refreshToken, HttpServletRequest httpReq, HttpServletResponse httpResp);

}
