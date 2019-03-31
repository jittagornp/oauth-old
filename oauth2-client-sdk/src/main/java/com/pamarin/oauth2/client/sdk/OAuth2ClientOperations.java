/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

/**
 *
 * @author jitta
 */
public interface OAuth2ClientOperations {

    OAuth2AccessToken getAccessTokenByAuthorizationCode(String authorizationCode);

    OAuth2AccessToken getAccessTokenByRefreshToken(String refreshToken);

    OAuth2Session getSession(String accessToken);

    <T> T get(String url, Class<T> responseType, String accessToken);

    <T> T post(String url, Object request, Class<T> responseType, String accessToken);

}
