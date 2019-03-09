/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/19
 */
public class UserDetailsStub {

    private UserDetailsStub() {

    }

    public static UserDetails get() {
        return DefaultUserDetails.builder()
                .username("00000000000000000000000000000000")
                .password("00000000000000000000000000000000")
                .build();
    }

}
