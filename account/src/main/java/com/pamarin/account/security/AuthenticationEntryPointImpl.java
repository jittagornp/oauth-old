/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.account.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class AuthenticationEntryPointImpl extends AuthenticationEntryPointAdapter {

    @Value("${oauth2.authorization-server.hostUrl}")
    private String authorizationServerHostUrl;
    
    @Value("${server.hostUrl}")
    private String hostUrl;

    @Override
    protected String getAuthorizationServerHostUrl() {
        return authorizationServerHostUrl;
    }

    @Override
    protected String getHostUrl() {
        return hostUrl + "/callback";
    }

    @Override
    protected String getClientId() {
        return "b98e21b4-ce2a-11e7-abc4-cec278b6b50a";
    }

    @Override
    protected String getScope() {
        return "user:public_profile";
    }

}
