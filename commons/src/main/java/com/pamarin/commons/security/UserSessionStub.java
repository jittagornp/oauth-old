/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.security.DefaultUserSession;
import com.pamarin.commons.security.UserSession;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/19
 */
public class UserSessionStub {

    private UserSessionStub() {

    }

    public static UserSession get() {
        return DefaultUserSession.builder()
                .id(1L)
                .username("test")
                .password(null)
                .build();
    }

}
