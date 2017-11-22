/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.exception;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/21
 */
public class InvalidUsernamePasswordException extends RuntimeException {

    public InvalidUsernamePasswordException() {
        super();
    }

    public InvalidUsernamePasswordException(String message) {
        super(message);
    }

}
