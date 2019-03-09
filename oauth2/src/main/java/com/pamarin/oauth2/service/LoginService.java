/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/21
 */
public interface LoginService extends UserDetailsService {

    void login(String username, String password);

}
