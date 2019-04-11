/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.OAuth2JobScheduler;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jitta
 */
public interface OAuth2JobSchedulerRepository extends JpaRepository<OAuth2JobScheduler, Long> {

}
