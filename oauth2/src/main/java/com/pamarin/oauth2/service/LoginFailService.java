/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

/**
 *
 * @author jitta
 */
public interface LoginFailService {

    void collect(String username);

    void verify(String username);

}
