/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.exception;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }

}
