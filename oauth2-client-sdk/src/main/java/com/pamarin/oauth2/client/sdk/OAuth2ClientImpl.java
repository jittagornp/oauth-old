/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.oauth2.client.sdk;

import java.util.Base64;
import java.util.Collections;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jitta
 */
public class OAuth2ClientImpl implements OAuth2Client {

    private final String clientId;

    private final String clientSecret;

    private final String authorizationServerHostUrl;

    private final RestTemplate restTemplate;

    public OAuth2ClientImpl(String clientId, String clientSecret, String authorizationServerHostUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationServerHostUrl = authorizationServerHostUrl;
        this.restTemplate = new RestTemplate();
    }

    private MultiValueMap<String, String> buildAccessTokenHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()));
        return headers;
    }

    private MultiValueMap<String, String> buildSessionHeaders(String accessToken) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Authorization", "Bearer " + accessToken);
        return headers;
    }

    private MultiValueMap<String, String> buildAuthorizationCodeBody(String authorizationCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", "");
        body.add("code", authorizationCode);
        return body;
    }

    private MultiValueMap<String, String> buildRefreshTokenBody(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("redirect_uri", "");
        body.add("refresh_token", refreshToken);
        return body;
    }

    @Override
    public OAuth2AccessToken getAccessTokenByAuthorizationCode(String authorizationCode) {
        return restTemplate.postForEntity(authorizationServerHostUrl + "/token",
                new HttpEntity<>(buildAuthorizationCodeBody(authorizationCode), buildAccessTokenHeaders()),
                OAuth2AccessToken.class
        ).getBody();
    }

    @Override
    public OAuth2AccessToken getAccessTokenByRefreshToken(String refreshToken) {
        return restTemplate.postForEntity(authorizationServerHostUrl + "/token",
                new HttpEntity<>(buildRefreshTokenBody(refreshToken), buildAccessTokenHeaders()),
                OAuth2AccessToken.class
        ).getBody();
    }

    @Override
    public OAuth2Session getSession(String accessToken) {
        return restTemplate.postForEntity(authorizationServerHostUrl + "/session",
                new HttpEntity<>(null, buildSessionHeaders(accessToken)),
                OAuth2Session.class
        ).getBody();
    }

}
