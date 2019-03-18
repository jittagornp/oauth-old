/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.oauth2.domain.UserSession;
import com.pamarin.oauth2.domain.UserSource;
import com.pamarin.oauth2.repository.UserSessionRepo;
import com.pamarin.oauth2.repository.UserSourceRepo;
import com.pamarin.oauth2.resolver.UserSourceTokenIdResolver;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import com.pamarin.oauth2.service.DatabaseSessionSynchronizer;
import javax.servlet.http.HttpServletRequest;
import static org.springframework.util.StringUtils.hasText;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import com.pamarin.commons.resolver.PrincipalNameResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.session.Session;

/**
 *
 * @author jitta
 */
public class DatabaseSessionSynchronizerImpl implements DatabaseSessionSynchronizer {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseSessionSynchronizerImpl.class);

    private static final String LAST_ACCESSED_TIME_ATTR = "lastAccessedTimeUserSession";

    private final Long synchronizeTimeout;

    private final Integer sessionTimeout;

    @Autowired
    private HttpServletRequestProvider httpServletRequestProvider;

    @Autowired
    private UserSessionRepo userSessionRepo;

    @Autowired
    private UserSourceRepo userSourceRepo;

    @Autowired
    private UserSourceTokenIdResolver userSourceTokenIdResolver;

    @Autowired
    private HttpClientIPAddressResolver httpClientIPAddressResolver;

    @Autowired
    private PrincipalNameResolver principalNameResolver;

    public DatabaseSessionSynchronizerImpl(Integer sessionTimeout, Long synchronizeTimeout) {
        this.sessionTimeout = sessionTimeout;
        this.synchronizeTimeout = synchronizeTimeout;
    }

    private String resolveUserSourceId(HttpServletRequest httpReq) {
        String sourceId = userSourceTokenIdResolver.resolve(httpReq);
        return sourceId;
//        if (!hasText(sourceId)) {
//            return null;
//        }
//
//        UserSource userSource = userSourceRepo.findOne(sourceId);
//        if (userSource == null) {
//            userSource = new UserSource();
//            userSource.setId(sourceId);
//            userSourceRepo.save(userSource);
//        }
//        return userSource.getId();
    }

    @Override
    public void synchronize(Session session) {
        long currentTime = System.currentTimeMillis();
        Long lastAcccessedTime = (Long) session.getAttribute(LAST_ACCESSED_TIME_ATTR);
        if (lastAcccessedTime == null || currentTime - lastAcccessedTime > synchronizeTimeout) {
            synchronizeUserSession(session);
            session.setAttribute(LAST_ACCESSED_TIME_ATTR, currentTime);
        }
    }

    private void synchronizeUserSession(Session session) {
        LOG.debug("synchronizeUserSession({})", session.getId());
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        String userId = principalNameResolver.resolve(session);
        String ipAddress = httpClientIPAddressResolver.resolve(httpReq);
        String sourceId = resolveUserSourceId(httpReq);
        LocalDateTime now = LocalDateTime.now();

        UserSession userSession = userSessionRepo.findOne(session.getId());
        if (userSession == null) {
            userSession = new UserSession();
            userSession.setId(session.getId());
            userSession.setCreateUser(userId);
            userSession.setCreatedDate(now);
            userSession.setTimeout(sessionTimeout);
            userSession = userSessionRepo.save(userSession);
        }

        userSession.setUserId(userId);
        userSession.setSourceId(sourceId);
        userSession.setIpAddress(ipAddress);
        userSession.setTimeout(sessionTimeout);
        userSession.setUpdatedDate(now);
        userSession.setUpdatedUser(hasText(userId) ? userId : "system");
    }
}
