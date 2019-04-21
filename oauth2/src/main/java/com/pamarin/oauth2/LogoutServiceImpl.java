/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.service.LoginHistoryService;
import com.pamarin.oauth2.service.LogoutService;
import com.pamarin.oauth2.service.RevokeSessionService;
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
    private RevokeSessionService revokeSessionService;

    @Autowired
    private LoginHistoryService loginHistoryService;

    @Override
    public void logout() {
        loginHistoryService.stampLogout();
        String sessionId = loginSession.getSessionId();
        loginSession.logout();
        revokeSessionService.revokeBySessionId(sessionId);
    }
}
