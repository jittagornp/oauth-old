/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.exception;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/17
 */
public class InvalidCsrfTokenException extends RuntimeException {

    public InvalidCsrfTokenException(String message) {
        super(message);
    }

}
