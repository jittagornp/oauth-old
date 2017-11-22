/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.exception;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/03
 */
public class InvalidScopeException extends RuntimeException {

    private final String scope;

    public InvalidScopeException(String scope, String message) {
        super(message);
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

}
