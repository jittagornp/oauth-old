/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception;

import com.pamarin.oauth2.domain.OAuth2Scope;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/22
 */
public class OAuth2ScopeNotFoundExpception extends RuntimeException {

    public OAuth2ScopeNotFoundExpception(String message) {
        super(message);
    }

    public static void throwByScope(String scope) {
        throw new OAuth2ClientNotFoundException(String.format("Not found %s of id \"%s\"", OAuth2Scope.class.getName(), scope));
    }
}
