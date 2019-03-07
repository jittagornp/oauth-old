/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.provider.HttpSessionProvider;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.domain.UserSession;
import com.pamarin.oauth2.repository.UserSessionRepo;
import java.time.LocalDateTime;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.pamarin.oauth2.service.DatabaseSessionSynchronizer;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author jitta
 */
@Service
public class DatabaseSessionSynchronizerImpl implements DatabaseSessionSynchronizer {

    private static final String LAST_ACCESSED_TIME = "lastAccessedTimeUserSession";

    private static final long SYNCHRONIZE_TIMEOUT = 1000 * 30; //30 seconds

    @Autowired
    private HttpSessionProvider httpSessionProvider;

    @Autowired
    private UserSessionRepo userSessionRepo;

    @Autowired
    private LoginSession loginSession;

    @Value("${spring.session.timeout}")
    private Integer sessionTimeout;

    @Override
    public void synchronize() {
        HttpSession session = httpSessionProvider.provide();
        if (session == null) {
            throw new IllegalStateException("Don't have session.");
        }

        Long lastAcccessedTime = (Long) session.getAttribute(LAST_ACCESSED_TIME);
        if (lastAcccessedTime == null) {
            updateUserSession(session);
            session.setAttribute(LAST_ACCESSED_TIME, System.currentTimeMillis());
        } else {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastAcccessedTime > SYNCHRONIZE_TIMEOUT) {
                updateUserSession(session);
                session.setAttribute(LAST_ACCESSED_TIME, currentTime);
            }
        }
    }

    private void updateUserSession(HttpSession session) {
        UserDetails userDetails = loginSession.getUserDetails();
        String sessionId = session.getId();
        String userId = userDetails.getUsername();

        UserSession userSession = userSessionRepo.findOne(sessionId);
        if (userSession == null) {
            userSession = new UserSession();
        } else {
            userSession.setUpdatedDate(LocalDateTime.now());
            userSession.setUpdatedUser(userId);
        }

        userSession.setId(sessionId);
        userSession.setUserId(userId);
        userSession.setTimeout(sessionTimeout);
        userSessionRepo.save(userSession);
    }

}
