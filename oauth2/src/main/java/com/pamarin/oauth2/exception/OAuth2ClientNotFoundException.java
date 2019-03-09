/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception;

import com.pamarin.oauth2.domain.OAuth2Client;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/22
 */
public class OAuth2ClientNotFoundException extends RuntimeException {

    public OAuth2ClientNotFoundException(String message) {
        super(message);
    }

    public static void throwByClientId(String clientId) {
        throw new OAuth2ClientNotFoundException(String.format("Not found %s of id \"%s\"", OAuth2Client.class.getName(), clientId));
    }

}
