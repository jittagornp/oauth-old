/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.exception;

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
