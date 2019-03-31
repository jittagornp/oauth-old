/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.provider.DefaultHttpServletRequestProvider;
import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.commons.resolver.DefaultHttpClientIPAddressResolver;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import com.pamarin.commons.util.Base64Utils;
import com.pamarin.commons.util.MultiValueMapBuilder;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jitta
 */
public class DefaultOAuth2ClientOperations implements OAuth2ClientOperations {

    private final String basicAuthorization;

    private final String authorizationServerHostUrl;

    private final RestTemplate restTemplate;

    private final HttpServletRequestProvider httpServletRequestProvider;

    private final HttpClientIPAddressResolver httpClientIPAddressResolver;

    public DefaultOAuth2ClientOperations(String clientId, String clientSecret, String authorizationServerHostUrl) {
        this.basicAuthorization = Base64Utils.encode(clientId + ":" + clientSecret);
        this.authorizationServerHostUrl = authorizationServerHostUrl;
        this.restTemplate = new RestTemplate();
        this.httpServletRequestProvider = new DefaultHttpServletRequestProvider();
        this.httpClientIPAddressResolver = new DefaultHttpClientIPAddressResolver();
    }

    private MultiValueMapBuilder addDefaultHeaders(MultiValueMapBuilder<String, String> builder) {
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        if (httpReq != null) {
            String ipAddress = httpClientIPAddressResolver.resolve(httpReq);
            builder.add("X-Forwarded-For", ipAddress)
                    .add("REMOTE_ADDR", ipAddress)
                    .add("User-Agent", httpReq.getHeader("User-Agent"))
                    .add("Referer", httpReq.getHeader("Referer"))
                    .add("Host", httpReq.getHeader("Host"));
        }
        return builder;
    }

    private MultiValueMap<String, String> buildAccessTokenHeaders() {
        return addDefaultHeaders(MultiValueMapBuilder.newLinkedMultiValueMap()
                .add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .add("Authorization", "Basic " + basicAuthorization))
                .build();
    }

    private MultiValueMap<String, String> buildSessionHeaders(String accessToken) {
        return addDefaultHeaders(MultiValueMapBuilder.newLinkedMultiValueMap()
                .add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .add("Authorization", "Bearer " + accessToken))
                .build();
    }

    private MultiValueMap<String, String> buildAuthorizationCodeBody(String authorizationCode) {
        return MultiValueMapBuilder.newLinkedMultiValueMap()
                .add("grant_type", "authorization_code")
                .add("redirect_uri", "")
                .add("code", authorizationCode)
                .build();
    }

    private MultiValueMap<String, String> buildRefreshTokenBody(String refreshToken) {
        return MultiValueMapBuilder.newLinkedMultiValueMap()
                .add("grant_type", "refresh_token")
                .add("redirect_uri", "")
                .add("refresh_token", refreshToken)
                .build();
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
