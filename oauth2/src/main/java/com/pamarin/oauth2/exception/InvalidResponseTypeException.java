/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/27
 */
public class InvalidResponseTypeException extends RuntimeException {

    private final String responseType;

    public InvalidResponseTypeException(String responseType, String message) {
        super(message);
        this.responseType = responseType;
    }

    public String getResponseType() {
        return responseType;
    }

}
