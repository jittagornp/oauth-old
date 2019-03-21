/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jitta
 */
public interface LoginHistoryRepo extends JpaRepository<LoginHistory, String>{
    
}
