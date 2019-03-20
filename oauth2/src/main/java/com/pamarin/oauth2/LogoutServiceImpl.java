/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.repository.UserSessionRepo;
import com.pamarin.oauth2.service.LogoutService;
import com.pamarin.oauth2.service.RevokeSessionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private RevokeSessionService revokeSessionService;

    @Override
    public void logout() {
        clearAllSessions();
        loginSession.logout();
    }

    private void clearAllSessions() {
        String sessionId = loginSession.getSessionId();
        List<String> sessionIds = userSessionRepo.findAllIdsRelativeWithId(sessionId);
        revokeSessionService.revoke(sessionIds);
    }
}
