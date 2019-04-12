/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository.mongodb;

import com.pamarin.oauth2.collection.LoginHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author jitta
 */
public interface LoginHistoryRepository extends MongoRepository<LoginHistory, String>{
    
}
