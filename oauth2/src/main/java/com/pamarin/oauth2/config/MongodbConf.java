/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.oauth2.repository.mongodb.MongodbOAuth2AccessTokenRepo;
import com.pamarin.oauth2.repository.mongodb.MongodbOAuth2RefreshTokenRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 *
 * @author jitta
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.pamarin.oauth2.repository.mongodb")
public class MongodbConf {

    @Bean
    public MongodbOAuth2AccessTokenRepo newMongodbOAuth2AccessTokenRepo(){
        return new MongodbOAuth2AccessTokenRepo();
    }
    
    @Bean
    public MongodbOAuth2RefreshTokenRepo newMongodbOAuth2RefreshTokenRepo(){
        return new MongodbOAuth2RefreshTokenRepo();
    }
    
}
