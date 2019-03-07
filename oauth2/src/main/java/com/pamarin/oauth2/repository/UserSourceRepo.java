/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.UserSource;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jitta
 */
public interface UserSourceRepo extends JpaRepository<UserSource, String>{
    
}
