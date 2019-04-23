/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.scheduling;

import com.pamarin.commons.generator.UUIDGenerator;
import com.pamarin.commons.provider.ServerInfomationProvider;
import static com.pamarin.commons.util.DateConverterUtils.convert2Date;
import static com.pamarin.commons.util.DateConverterUtils.convert2LocalDateTime;
import com.pamarin.oauth2.domain.OAuth2JobScheduler;
import static com.pamarin.oauth2.domain.OAuth2JobScheduler.TABLE_NAME;
import static java.lang.String.format;
import java.time.LocalDateTime;
import static java.util.Collections.singletonList;
import java.util.Objects;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jitta
 */
@Slf4j
@Service
@Transactional
public class DefaultChampionshipJobSchedulerService implements ChampionshipJobSchedulerService, JobRunner {

    private static final long RUN_EVERY_SECONDS = 60; 

    private static final long CHAMPOIN_EXPIRE_SECONDS = RUN_EVERY_SECONDS + 10;

    private static final String SELECT_CHAMPION_SQL = format("select * from %s", TABLE_NAME);

    private static final String INSERT_CHAMPION_SQL = format("insert into %s (id, job_id, updated_date, server_ip) values (?, ?, ?, ?)", TABLE_NAME);

    private static final String UPDATE_CHAMPION_SQL = format("update %s set updated_date = ?, server_ip = ?", TABLE_NAME);

    private static final String DELETE_CHAMPION_SQL = format("delete from %s", TABLE_NAME);

    private static final Long FIXED_ID = 1L;

    private final String jobId;

    private final JdbcTemplate jdbcTemplate;

    private final ServerInfomationProvider serverInfomationProvider;

    @Autowired
    public DefaultChampionshipJobSchedulerService(
            DataSource dataSource,
            UUIDGenerator uuidGenerator,
            ServerInfomationProvider serverInfomationProvider
    ) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jobId = uuidGenerator.generate();
        this.serverInfomationProvider = serverInfomationProvider;
    }

    @Override
    @Scheduled(fixedDelay = RUN_EVERY_SECONDS * 1000)
    public void run() {
        log.debug("Championship job runner : \"{}\" ...", jobId);
        OAuth2JobScheduler champion = findChampion();
        if (notFound(champion)) {
            log.debug("Not found champion.");
            toBeChampion();
        } else if (thisIsChampion(champion)) {
            continueToChampion(champion);
        } else if (wasExpired(champion)) {
            log.debug("\"{}\" was expired.", champion.getJobId());
            deleteChampion();
            toBeChampion();
        } else {
            log.debug("\"{}\" is challenger, don't have to do anything.", jobId);
        }
    }

    private boolean notFound(OAuth2JobScheduler champion) {
        return champion == null;
    }

    private boolean thisIsChampion(OAuth2JobScheduler champion) {
        return Objects.equals(jobId, champion.getJobId());
    }

    private boolean wasExpired(OAuth2JobScheduler champion) {
        LocalDateTime updatedDate = champion.getUpdatedDate();
        if (updatedDate == null) {
            return true;
        }
        return updatedDate.plusSeconds(CHAMPOIN_EXPIRE_SECONDS)
                .isBefore(LocalDateTime.now());
    }

    private OAuth2JobScheduler findChampion() {
        try {
            log.debug("{}", SELECT_CHAMPION_SQL);
            return jdbcTemplate.queryForObject(SELECT_CHAMPION_SQL, (rs, i)
                    -> singletonList(OAuth2JobScheduler.builder()
                            .id(rs.getLong("id"))
                            .jobId(rs.getString("job_id"))
                            .updatedDate(convert2LocalDateTime(rs.getTimestamp("updated_date")))
                            .serverIp(rs.getString("server_ip"))
                            .build())
            ).get(0);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    private void continueToChampion(OAuth2JobScheduler champion) {
        log.debug("\"{}\" continue to champion.", champion.getJobId());
        log.debug("{}", UPDATE_CHAMPION_SQL);
        jdbcTemplate.update(
                UPDATE_CHAMPION_SQL, 
                convert2Date(LocalDateTime.now()),
                serverInfomationProvider.provideIpAddress()
        );
    }

    private void toBeChampion() {
        log.debug("\"{}\" to be champion.", jobId);
        log.debug("{}", INSERT_CHAMPION_SQL);
        jdbcTemplate.update(INSERT_CHAMPION_SQL,
                FIXED_ID,
                jobId,
                convert2Date(LocalDateTime.now()),
                serverInfomationProvider.provideIpAddress()
        );
    }

    private void deleteChampion() {
        log.debug("Delete champion");
        log.debug("{}", DELETE_CHAMPION_SQL);
        jdbcTemplate.update(DELETE_CHAMPION_SQL);
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
        return !wasExpired(champion);
    }
}
