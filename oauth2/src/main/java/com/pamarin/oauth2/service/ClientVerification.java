/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/25
 */
public interface ClientVerification {

    void verifyClientIdAndRedirectUri(String clientId, String redirectUri);

    void verifyClientIdAndClientSecret(String clientId, String clientSecret);

}
