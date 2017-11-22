/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/21
 */
@FunctionalInterface
public interface UserService {

    void login(String username, String password);

}
