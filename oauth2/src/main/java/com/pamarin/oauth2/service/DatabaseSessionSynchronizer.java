/*
 * Copyright 2017 Pamarin.com
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
