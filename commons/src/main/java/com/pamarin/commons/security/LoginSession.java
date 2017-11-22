/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/25
 */
public interface LoginSession {
    
    void create(UserSession userSession);

    boolean wasCreated();

    UserSession getUserSession();

}
