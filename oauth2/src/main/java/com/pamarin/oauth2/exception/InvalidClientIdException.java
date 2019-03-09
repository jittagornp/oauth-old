/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/25
 */
public class InvalidClientIdException extends RuntimeException {

    private final String clientId;

    public InvalidClientIdException(String clientId, String message) {
        super(message);
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

}
