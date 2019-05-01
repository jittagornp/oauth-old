/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import com.pamarin.oauth2.service.RevokeSessionService;
import com.pamarin.oauth2.service.RevokeTokenService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Slf4j
@Service
public class RevokeSessionServiceImpl implements RevokeSessionService {

    private final UserSessionRepository userSessionRepository;

    private final SessionRepository sessionRepository;

    private final RevokeTokenService revokeTokenService;

    @Autowired
    public RevokeSessionServiceImpl(
            UserSessionRepository userSessionRepository,
            SessionRepository sessionRepository,
            RevokeTokenService revokeTokenService
    ) {
        this.userSessionRepository = userSessionRepository;
        this.sessionRepository = sessionRepository;
        this.revokeTokenService = revokeTokenService;
    }

    @Override
    public void revokeBySessionId(String sessionId) {
        if (hasText(sessionId)) {
            sessionRepository.delete(sessionId);
            revokeTokenService.revokeBySessionId(sessionId);
        }
    }

    @Override
    public void revokeBySessionIdWithoutToken(String sessionId) {
        if (hasText(sessionId)) {
            sessionRepository.delete(sessionId);
        }
    }

    @Override
    public void revokeBySessionIds(List<String> sessionIds) {
        if (!isEmpty(sessionIds)) {
            sessionIds.forEach(this::revokeBySessionId);
        }
    }

    @Override
    public void revokeBySessionIdsWithoutToken(List<String> sessionIds) {
        if (!isEmpty(sessionIds)) {
            sessionIds.forEach(this::revokeBySessionIdWithoutToken);
        }
    }

    @Override
    public void revokeAllOnSameUserAgentBySessionId(String sessionId) {
        if (hasText(sessionId)) {
            revokeBySessionIds(userSessionRepository.findAllSessionIdsOnSameUserAgentBySessionId(sessionId));
        }
    }

    @Override
    public void revokeByUserId(String userId) {
        if (hasText(userId)) {
            revokeBySessionIds(userSessionRepository.findAllSessionIdsByUserId(userId));
        }
    }

    @Override
    public void revokeOthersOnSameUserAgentBySessionId(String sessionId) {
        if (hasText(sessionId)) {
            revokeBySessionIds(userSessionRepository.findOtherSessionIdsOnSameUserAgentBySessionId(sessionId));
        }
    }

    @Override
    public void revokeExpiredSessions() {
        revokeBySessionIdsWithoutToken(userSessionRepository.findExpiredSessions());
    }
}
