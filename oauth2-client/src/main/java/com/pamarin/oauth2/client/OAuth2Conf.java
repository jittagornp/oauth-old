/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client;

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

    @Value("${oauth2.client-id}")
    private String clientId;

    @Value("${oauth2.client-secret}")
    private String clientSecret;

    @Bean
    public OAuth2ClientOperations newOAuth2ClientOperations() {
        return new DefaultOAuth2ClientOperations(
                clientId,
                clientSecret,
                authorizationServerHostUrl,
                "user:public_profile"
        );
    }

}
