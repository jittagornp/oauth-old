/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.scheduling;

import com.pamarin.oauth2.service.RevokeSessionService;
import com.pamarin.oauth2.service.RevokeTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author jitta
 */
@Slf4j
@Service
public class CleanExpiredSessionTokenJobRunner implements JobRunner {

    private static final long RUN_EVERY_MINUTES = 60;

    private final ChampionshipJobSchedulerService jobSchedulerService;

    private final RevokeSessionService revokeSessionService;

    private final RevokeTokenService revokeTokenService;

    @Autowired
    public CleanExpiredSessionTokenJobRunner(
            ChampionshipJobSchedulerService jobSchedulerService,
            RevokeSessionService revokeSessionService,
            RevokeTokenService revokeTokenService
    ) {
        this.jobSchedulerService = jobSchedulerService;
        this.revokeSessionService = revokeSessionService;
        this.revokeTokenService = revokeTokenService;
    }

    @Override
    @Scheduled(fixedDelay = RUN_EVERY_MINUTES * 60 * 1000)
    public void run() {
        if (jobSchedulerService.isChampion()) {
            revokeSessionService.revokeExpiredSessions();
            revokeTokenService.revokeExpiredTokens();
        }
    }

}
