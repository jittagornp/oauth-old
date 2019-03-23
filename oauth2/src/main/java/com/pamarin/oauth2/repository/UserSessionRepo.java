/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.UserSession;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author jitta
 */
public interface UserSessionRepo extends JpaRepository<UserSession, String> {

    UserSession findBySessionId(String sessionId);

    @Modifying
    @Query(
            value = "DELETE FROM " + UserSession.TABLE_NAME + " s WHERE s.session_id = ?1",
            nativeQuery = true
    )
    void deleteBySessionId(String sessionId);

    @Query(
            value = "SELECT s2.session_id "
            + "FROM " + UserSession.TABLE_NAME + " s1 "
            + "INNER JOIN " + UserSession.TABLE_NAME + " s2 "
            + "ON (s1.agent_id = s2.agent_id) "
            + "WHERE s1.session_id = ?1 ",
            nativeQuery = true
    )
    List<String> findAllSessionIdsOnSameUserAgentBySessionId(String id);

    @Query(
            value = "SELECT s2.session_id "
            + "FROM " + UserSession.TABLE_NAME + " s1 "
            + "INNER JOIN " + UserSession.TABLE_NAME + " s2 "
            + "ON (s1.agent_id = s2.agent_id) "
            + "WHERE s1.session_id = ?1 AND s2.session_id <> ?1",
            nativeQuery = true
    )
    List<String> findOtherSessionIdsOnSameUserAgentBySessionId(String id);

    @Query(
            value = "SELECT s.session_id "
            + "FROM " + UserSession.TABLE_NAME + " s "
            + "WHERE s.user_id = ?1 ",
            nativeQuery = true
    )
    List<String> findAllSessionIdsByUserId(String userId);
}
