/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.exception;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/25
 */
public class InvalidClientIdAndClientSecretException extends RuntimeException {

    private final String clientId;

    private final String clientSecret;

    public InvalidClientIdAndClientSecretException(String clientId, String clientSecret, String message) {
        super(message);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

}
