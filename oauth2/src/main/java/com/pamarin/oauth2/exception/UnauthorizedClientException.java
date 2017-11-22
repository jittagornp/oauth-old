/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.exception;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
public class UnauthorizedClientException extends RuntimeException {

    public UnauthorizedClientException(Throwable cause) {
        super(cause);
    }

    public UnauthorizedClientException(String message) {
        super(message);
    }

}
