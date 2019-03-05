/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jitta
 */
public interface UserSessionRepo extends JpaRepository<UserSession, String>{
    
}
