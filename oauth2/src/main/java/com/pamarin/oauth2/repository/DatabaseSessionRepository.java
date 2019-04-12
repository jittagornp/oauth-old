/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import org.springframework.session.Session;

/**
 *
 * @author jitta
 */
public interface DatabaseSessionRepository {

    void synchronize(Session session);
    
}
