/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.scheduling;

import com.pamarin.oauth2.ratelimit.TokenBucketRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 *
 * @author jitta
 */
@Service
public class CleanTokenBucketRepositoryJobRunner implements JobRunner {

    private static final long RUN_EVERY_MINUTES = 1;

    private final List<TokenBucketRepository> tokenBucketRepositories;

    @Autowired
    public CleanTokenBucketRepositoryJobRunner(List<TokenBucketRepository> tokenBucketRepositories) {
        this.tokenBucketRepositories = tokenBucketRepositories;
    }

    @Override
    @Scheduled(fixedDelay = RUN_EVERY_MINUTES * 60 * 1000)
    public void run() {
        if (!isEmpty(tokenBucketRepositories)) {
            tokenBucketRepositories.forEach(TokenBucketRepository::delete);
        }
    }

}
