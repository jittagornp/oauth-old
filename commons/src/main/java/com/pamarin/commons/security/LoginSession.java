/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/25
 */
public interface LoginSession {
    
    void create(UserDetails userSession);

    boolean wasCreated();

    UserDetails getUserDetails();

    Authentication getAuthentication();
     
    String getSessionId();
}
