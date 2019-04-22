/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author jitta
 */
@Service
public class CleanTokenCacheJobRunner implements JobRunner {

    private static final long RUN_EVERY_HOURS = 3 * (60 * 60 * 1000);

    private final RedisOperations<String, String> redisOperations;

    private final ChampionshipJobSchedulerService jobSchedulerService;

    @Autowired
    public CleanTokenCacheJobRunner(
            RedisOperations<String, String> redisOperations,
            ChampionshipJobSchedulerService jobSchedulerService
    ) {
        this.redisOperations = redisOperations;
        this.jobSchedulerService = jobSchedulerService;
    }

    @Override
    @Scheduled(fixedDelay = RUN_EVERY_HOURS)
    public void run() {
        if (jobSchedulerService.isChampion()) {
            deleteAll("oauth2_refresh_token:.*");
            deleteAll("oauth2_access_token:.*");
            deleteAll("user_session:.*");
        }
    }

    private void deleteAll(String keyPattern) {
        redisOperations.delete(redisOperations.keys(keyPattern));
    }

}
