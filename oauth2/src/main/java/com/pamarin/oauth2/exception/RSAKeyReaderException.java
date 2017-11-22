/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.exception;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/11
 */
public class RSAKeyReaderException extends RuntimeException {

    public RSAKeyReaderException(Throwable cause) {
        super(cause);
    }

    public RSAKeyReaderException(String message) {
        super(message);
    }

}
