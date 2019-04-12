/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.collection.UserSession;
import java.util.List;

/**
 *
 * @author jitta
 */
public interface UserSessionRepository {
    
    UserSession save(UserSession userSession);

    UserSession findBySessionId(String sessionId);

    void deleteBySessionId(String sessionId);

    List<String> findAllSessionIdsOnSameUserAgentBySessionId(String sessionId);

    List<String> findOtherSessionIdsOnSameUserAgentBySessionId(String sessionId);

    List<String> findAllSessionIdsByUserId(String userId);
}
