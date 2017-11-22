/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.exception;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/08
 */
public class InvalidRedirectUriException extends RuntimeException {

    private final String redirectUri;

    public InvalidRedirectUriException(String redirectUri, String message) {
        super(message);
        this.redirectUri = redirectUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

}
