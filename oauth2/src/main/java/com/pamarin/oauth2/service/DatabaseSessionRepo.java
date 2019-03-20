/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import org.springframework.session.Session;

/**
 *
 * @author jitta
 */
public interface DatabaseSessionRepo {

    void synchronize(Session session);
    
}
