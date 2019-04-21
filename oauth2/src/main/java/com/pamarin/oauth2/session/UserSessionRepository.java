/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import java.util.List;

/**
 *
 * @author jitta
 */
public interface UserSessionRepository {

    List<String> findAllSessionIdsOnSameUserAgentBySessionId(String sessionId);

    List<String> findOtherSessionIdsOnSameUserAgentBySessionId(String sessionId);

    List<String> findAllSessionIdsByUserId(String userId);
}
