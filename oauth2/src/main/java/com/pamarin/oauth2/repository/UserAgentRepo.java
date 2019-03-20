/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.UserAgent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jitta
 */
public interface UserAgentRepo extends JpaRepository<UserAgent, String>{
    
}
