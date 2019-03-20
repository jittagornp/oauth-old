/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.UserSession;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author jitta
 */
public interface UserSessionRepo extends JpaRepository<UserSession, String> {

    @Query(
            value = "SELECT s2.id "
            + "FROM " + UserSession.TABLE_NAME + " s1 "
            + "INNER JOIN " + UserSession.TABLE_NAME + " s2 "
            + "on (s1.agent_id = s2.agent_id) "
            + "where s1.id = ?1 ",
            nativeQuery = true
    )
    List<String> findAllIdsRelativeWithId(String id);
}
