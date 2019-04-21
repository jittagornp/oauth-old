/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author jitta
 */
@Slf4j
@Service
public class CleanSessionExpireJobRunner implements JobRunner {

    private static final long RUN_EVERY_MINUTES = 1;
//
//    private final MongoOperations mongoOperations;
//
//    @Autowired
//    public CleanSessionExpireJobRunner(MongoOperations mongoOperations) {
//        this.mongoOperations = mongoOperations;
//    }

    @Override
    //@Scheduled(fixedDelay = RUN_EVERY_MINUTES * 60 * 1000)
    public void run() {
    
    }

}
