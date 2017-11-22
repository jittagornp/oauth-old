/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.exception;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/09
 */
public class InvalidGrantTypeException extends RuntimeException {

    private final String grantType;

    public InvalidGrantTypeException(String grantType, String message) {
        super(message);
        this.grantType = grantType;
    }

    public String getGrantType() {
        return grantType;
    }

}
