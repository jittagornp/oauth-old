/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.domain.UserSession;
import com.pamarin.oauth2.repository.UserSessionRepo;
import com.pamarin.oauth2.service.LogoutService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Service
@Transactional
public class LogoutServiceImpl implements LogoutService {

    @Autowired
    private LoginSession loginSession;

    @Autowired
    private UserSessionRepo userSessionRepo;

    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public void logout() {
        clearAllSessions();
        loginSession.logout();
    }

    private void clearAllSessions() {
        String sessionId = loginSession.getSessionId();
        String agentId = userSessionRepo.findUserAgentIdBySessionId(sessionId);
        if (hasText(agentId)) {
            revokeSessionByUserAgentId(agentId);
        }
    }

    private void revokeSessionByUserAgentId(String agentId) {
        List<UserSession> userSessions = userSessionRepo.findByAgentId(agentId);
        if (isEmpty(userSessions)) {
            return;
        }

        userSessions.forEach(userSession -> {
            //revoke redis session
            sessionRepository.delete(userSession.getId());
        });

        userSessionRepo.delete(userSessions);
    }
}
