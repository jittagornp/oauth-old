/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.UserSession;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author jitta
 */
public interface UserSessionRepo extends JpaRepository<UserSession, String> {

    @Modifying
    @Query(
            "UPDATE UserSession us "
            + "SET us.updatedDate = ?1, "
            + "    us.updatedUser = ?2, "
            + "WHERE us.id = ?3 "
    )
    public void updateUpdatedDateAndUpdatedUserById(LocalDateTime date, String userId, String sessionId);

}
