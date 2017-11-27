/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/19
 */
public class UserSessionStub {

    private UserSessionStub() {

    }

    public static UserSession get() {
        return DefaultUserSession.builder()
                .id("00000000000000000000000000000000")
                .username("test")
                .password(null)
                .build();
    }

}
