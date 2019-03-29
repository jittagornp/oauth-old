/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.oauth2.client.sdk;

/**
 *
 * @author jitta
 */
public interface OAuth2Client {

    OAuth2AccessToken getAccessTokenByAuthorizationCode(String authorizationCode);

    OAuth2AccessToken getAccessTokenByRefreshToken(String refreshToken);

    OAuth2Session getSession(String accessToken);

}
