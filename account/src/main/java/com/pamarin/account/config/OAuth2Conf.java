/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.account.config;

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
