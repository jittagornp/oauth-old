/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.scheduling;

import com.pamarin.commons.util.DateConverterUtils;
import static com.pamarin.commons.util.DateConverterUtils.convert2Timestamp;
import com.pamarin.oauth2.collection.LoginFailHistory;
import static java.time.LocalDateTime.now;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;
import static org.springframework.data.mongodb.core.query.Query.query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author jitta
 */
@Slf4j
@Service
public class CleanExpiredLoginFailJobRunner implements JobRunner {

    private static final long RUN_EVERY_HOURS = 4;

    private final MongoOperations mongoOperations;

    private final ChampionshipJobSchedulerService jobSchedulerService;

    @Autowired
    public CleanExpiredLoginFailJobRunner(MongoOperations mongoOperations, ChampionshipJobSchedulerService jobSchedulerService) {
        this.mongoOperations = mongoOperations;
        this.jobSchedulerService = jobSchedulerService;
    }

    @Override
    @Scheduled(fixedDelay = RUN_EVERY_HOURS * 60 * 60 * 1000)
    public void run() {
        if (jobSchedulerService.isChampion()) {
            long now = convert2Timestamp(now());
            Query query = query(where("expirationTime").lte(now));
            mongoOperations.remove(query, LoginFailHistory.class);
            log.debug("clean expired login fail history ...");
        }
    }
}
