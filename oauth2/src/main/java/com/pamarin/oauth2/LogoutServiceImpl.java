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
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Service;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Service
public class LogoutServiceImpl implements LogoutService {

    @Autowired
    private LoginSession loginSession;

    @Autowired
    private UserSessionRepo userSessionRepo;

    @Autowired
    private RedisOperationsSessionRepository sessionRepository;

    @Override
    public void logout() {
        clearAllSessions();
        loginSession.logout();
    }

    private void clearAllSessions() {
        String sessionId = loginSession.getSessionId();
        String sourceId = userSessionRepo.findUserSourceIdBySessionId(sessionId);
        if (!hasText(sourceId)) {
            return;
        }

        List<UserSession> userSessions = userSessionRepo.findBySourceId(sourceId);
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
