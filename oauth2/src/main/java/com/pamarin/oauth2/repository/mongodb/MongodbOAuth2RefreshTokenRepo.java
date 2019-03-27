/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository.mongodb;

import com.pamarin.oauth2.domain.OAuth2RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author jitta
 */
public interface MongodbOAuth2RefreshTokenRepo extends MongoRepository<OAuth2RefreshToken, String>{
    
}
