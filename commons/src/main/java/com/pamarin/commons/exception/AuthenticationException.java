/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.exception;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/22
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public static void throwByMessage(String message) {
        throw new AuthenticationException(message);
    }

}
