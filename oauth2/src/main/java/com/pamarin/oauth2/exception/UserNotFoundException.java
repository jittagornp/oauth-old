/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.exception;

import com.pamarin.oauth2.domain.User;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/05
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public static void throwbyUserId(String userId) {
        throw new UserNotFoundException(String.format("Not found %s of id \"%s\"", User.class.getName(), userId));
    }

}
