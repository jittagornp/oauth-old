/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.usermanagement.config;

import com.pamarin.oauth2.client.sdk.DefaultOAuth2ClientOperations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.pamarin.oauth2.client.sdk.OAuth2ClientOperations;

/**
 *
 * @author jitta
 */
@Configuration
public class OAuth2Conf {

    @Value("${oauth2.authorization-server.hostUrl}")
    private String authorizationServerHostUrl;

    @Bean
    public OAuth2ClientOperations newOAuth2ClientOperations() {
        String clientId = "b98e21b4-ce2a-11e7-abc4-cec278b6b50a";
        String clientSecret = "password";
        return new DefaultOAuth2ClientOperations(clientId, clientSecret, authorizationServerHostUrl);
    }

}
