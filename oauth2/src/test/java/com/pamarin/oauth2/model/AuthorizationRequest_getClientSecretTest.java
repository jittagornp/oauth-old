/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.model;

import org.junit.Test; 

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/23
 */
public class AuthorizationRequest_getClientSecretTest {

    @Test(expected = UnsupportedOperationException.class)
    public void shouldBeThrowUnsupportedOperationException() {
        AuthorizationRequest.builder().build().getClientSecret();
    }

}
