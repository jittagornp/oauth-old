/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.scheduling;

import com.pamarin.commons.generator.UUIDGenerator;
import com.pamarin.oauth2.domain.OAuth2JobScheduler;
import com.pamarin.oauth2.repository.OAuth2JobSchedulerRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 *
 * @author jitta
 */
@Service
@Transactional
public class DefaultChampionshipJobSchedulerService implements ChampionshipJobSchedulerService, JobRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultChampionshipJobSchedulerService.class);

    private static final long CHAMPOIN_EXPIRE_SECONDS = 30;

    private static final long RUN_EVERY_MILLISECS = 20 * 1000; //20 seconds

    private final Long FIXED_ID = 1L;

    private final String jobId;

    private final OAuth2JobSchedulerRepository jobSchedulerRepository;

    @Autowired
    public DefaultChampionshipJobSchedulerService(OAuth2JobSchedulerRepository jobSchedulerRepository, UUIDGenerator uuidGenerator) {
        this.jobSchedulerRepository = jobSchedulerRepository;
        this.jobId = uuidGenerator.generate();
    }

    private OAuth2JobScheduler findChampion() {
        List<OAuth2JobScheduler> schedulers = jobSchedulerRepository.findAll();
        if (isEmpty(schedulers)) {
            return null;
        }
        return schedulers.get(0);
    }

    private boolean notFound(OAuth2JobScheduler champion) {
        return champion == null;
    }

    @Override
    @Scheduled(fixedDelay = RUN_EVERY_MILLISECS)
    public void run() {
        LOG.debug("Championship job runner : \"{}\" ...", jobId);
        OAuth2JobScheduler champion = findChampion();
        if (notFound(champion)) {
            LOG.debug("Not found champion.");
            toBeChampion();
        } else if (thisIsChampion(champion)) {
            continueToChampion(champion);
        } else if (wasExpired(champion.getUpdatedDate())) {
            LOG.debug("\"{}\" was expired.", champion.getJobId());
            deleteChampion();
        }
    }

    private boolean thisIsChampion(OAuth2JobScheduler champion) {
        return Objects.equals(jobId, champion.getJobId());
    }

    private boolean wasExpired(LocalDateTime updatedDate) {
        if (updatedDate == null) {
            return true;
        }
        return updatedDate.plusSeconds(CHAMPOIN_EXPIRE_SECONDS)
                .isBefore(LocalDateTime.now());
    }

    private void continueToChampion(OAuth2JobScheduler champion) {
        LOG.debug("\"{}\" continue to champion.", champion.getJobId());
        champion.setUpdatedDate(LocalDateTime.now());
    }

    private void toBeChampion() {
        LOG.debug("\"{}\" to be champion.", jobId);
        jobSchedulerRepository.save(OAuth2JobScheduler.builder()
                .id(FIXED_ID)
                .jobId(jobId)
                .updatedDate(LocalDateTime.now())
                .build());
    }

    private void deleteChampion() {
        LOG.debug("Delete champion");
        jobSchedulerRepository.deleteAll();
    }

    @Override
    public boolean isChampion() {
        OAuth2JobScheduler champion = findChampion();
        if (notFound(champion)) {
            return false;
        }
        if (!thisIsChampion(champion)) {
            return false;
        }
        return !wasExpired(champion.getUpdatedDate());
    }
}
