/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 *
 * @author jitta
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.pamarin.oauth2.repository")
public class MongodbConf {

}
