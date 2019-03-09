/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

/**
 *
 * @author jitta
 */
public interface DatabaseSessionSynchronizer {
    
    void createSession();

    void synchronize();
    
}
