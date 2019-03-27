/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.OAuth2AccessToken;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author jitta
 */
public interface MongodbOAuth2AccessTokenRepo extends MongoRepository<OAuth2AccessToken, String>{
    
}
