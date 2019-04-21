/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.scheduling;

import com.pamarin.oauth2.service.RevokeSessionService;
import static com.pamarin.oauth2.session.SessionAttributeConstant.EXPIRATION_TIME;
import com.pamarin.oauth2.session.UserSession;
import static java.util.Collections.emptyList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 *
 * @author jitta
 */
@Slf4j
@Service
public class CleanExpiredSessionJobRunner implements JobRunner {

    private static final long RUN_EVERY_MINUTES = 60;

    private final MongoOperations mongoOperations;

    private final ChampionshipJobSchedulerService jobSchedulerService;

    private final RevokeSessionService revokeSessionService;

    @Autowired
    public CleanExpiredSessionJobRunner(
            MongoOperations mongoOperations,
            ChampionshipJobSchedulerService jobSchedulerService,
            RevokeSessionService revokeSessionService
    ) {
        this.mongoOperations = mongoOperations;
        this.jobSchedulerService = jobSchedulerService;
        this.revokeSessionService = revokeSessionService;
    }

    @Override
    @Scheduled(fixedDelay = RUN_EVERY_MINUTES * 60 * 1000)
    public void run() {
        if (jobSchedulerService.isChampion()) {
            List<UserSession> sessions = findExpiredSessions();
            log.debug("found expired session => {}", sessions.size());
            revokeSessionService.revokeBySessionIds(
                    sessions.stream()
                            .map(userSession -> userSession.getSessionId())
                            .collect(toList())
            );
        }
    }

    private List<UserSession> findExpiredSessions() {
        Query query = Query.query(Criteria.where(EXPIRATION_TIME).lt(System.currentTimeMillis()));
        List<UserSession> sessions = mongoOperations.find(query, UserSession.class);
        if (isEmpty(sessions)) {
            return emptyList();
        }
        return sessions;
    }

}
