/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.provider.DefaultHttpServletRequestProvider;
import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.commons.resolver.DefaultHttpClientIPAddressResolver;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import com.pamarin.commons.util.Base64Utils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jitta
 */
public class DefaultOAuth2ClientOperations implements OAuth2ClientOperations {

    private final String clientId;

    private final String clientSecret;

    private final String authorizationServerHostUrl;

    private final RestTemplate restTemplate;

    private final HttpServletRequestProvider httpServletRequestProvider;

    private final HttpClientIPAddressResolver httpClientIPAddressResolver;

    public DefaultOAuth2ClientOperations(String clientId, String clientSecret, String authorizationServerHostUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationServerHostUrl = authorizationServerHostUrl;
        this.restTemplate = new RestTemplate();
        this.httpServletRequestProvider = new DefaultHttpServletRequestProvider();
        this.httpClientIPAddressResolver = new DefaultHttpClientIPAddressResolver();
    }

    private void addDefaultHeaders(MultiValueMap<String, String> headers) {
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        if (httpReq != null) {
            headers.add("X-Forwarded-For", httpClientIPAddressResolver.resolve(httpReq));
            headers.add("User-Agent", httpReq.getHeader("User-Agent"));
            headers.add("Referer", httpReq.getHeader("Referer"));
            headers.add("Host", httpReq.getHeader("Host"));
        }
    }

    private MultiValueMap<String, String> buildAccessTokenHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Authorization", "Basic " + Base64Utils.encode(clientId + ":" + clientSecret));
        addDefaultHeaders(headers);
        return headers;
    }

    private MultiValueMap<String, String> buildSessionHeaders(String accessToken) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Authorization", "Bearer " + accessToken);
        addDefaultHeaders(headers);
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
