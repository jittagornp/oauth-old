/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import java.util.List;

/**
 *
 * @author jitta
 */
public interface RevokeSessionService {

    void revokeBySessionId(String sessionId);

    void revokeBySessionIds(List<String> sessionIds);

    void revokeAllOnSameUserAgentBySessionId(String sessionId);

    void revokeAllOnSameUserAgentByIgnoreSessionId(String sessionId);

    void revokeByUserId(String userId);

}
