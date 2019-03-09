/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.exception;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
public class InvalidHttpAuthorizationException extends RuntimeException {

    public InvalidHttpAuthorizationException(String message) {
        super(message);
    }

}
