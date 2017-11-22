/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.exception;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/25
 */
public class InvalidClientIdAndRedirectUriException extends RuntimeException {

    private final String clientId;

    private final String redirectUri;

    public InvalidClientIdAndRedirectUriException(String clientId, String redirectUri, String message) {
        super(message);
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

}
