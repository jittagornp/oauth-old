/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.oauth2.domain.UserSession;
import com.pamarin.oauth2.domain.UserAgent;
import com.pamarin.oauth2.repository.UserSessionRepo;
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
import com.pamarin.oauth2.repository.UserAgentRepo;
import com.pamarin.oauth2.resolver.UserAgentTokenIdResolver;

/**
 *
 * @author jitta
 */
public class DatabaseSessionSynchronizerImpl implements DatabaseSessionSynchronizer {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseSessionSynchronizerImpl.class);

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    private static final String LAST_ACCESSED_TIME_ATTR = "lastAccessedTimeUserSession";

    private static final String LAST_ACCESSED_TIME_WITH_LOGIN_ATTR = "lastAccessedTimeUserSessionWithLogin";

    private final Long synchronizeTimeout;

    private final Integer sessionTimeout;

    @Autowired
    private HttpServletRequestProvider httpServletRequestProvider;

    @Autowired
    private UserSessionRepo userSessionRepo;

    @Autowired
    private UserAgentRepo userAgentRepo;

    @Autowired
    private UserAgentTokenIdResolver userAgentTokenIdResolver;

    @Autowired
    private HttpClientIPAddressResolver httpClientIPAddressResolver;

    @Autowired
    private PrincipalNameResolver principalNameResolver;

    public DatabaseSessionSynchronizerImpl(Integer sessionTimeout, Long synchronizeTimeout) {
        this.sessionTimeout = sessionTimeout;
        this.synchronizeTimeout = synchronizeTimeout;
    }

    private String resolveUserAgentId(HttpServletRequest httpReq) {
        String agentId = userAgentTokenIdResolver.resolve(httpReq);
        if (!hasText(agentId)) {
            return null;
        }

        UserAgent userAgent = userAgentRepo.findOne(agentId);
        if (userAgent == null) {
            userAgent = new UserAgent();
            userAgent.setId(agentId);
            userAgentRepo.save(userAgent);
        }
        return userAgent.getId();
    }

    @Override
    public void synchronize(Session session) {
        long currentTime = System.currentTimeMillis();
        Long lastAcccessedTime = (Long) session.getAttribute(LAST_ACCESSED_TIME_ATTR);
        if (lastAcccessedTime == null || currentTime - lastAcccessedTime > synchronizeTimeout) {
            synchronizeUserSession(session);
            session.setAttribute(LAST_ACCESSED_TIME_ATTR, currentTime);
        } else {
            Object firstTimeWithLogin = session.getAttribute(LAST_ACCESSED_TIME_WITH_LOGIN_ATTR);
            if (firstTimeWithLogin == null) {
                boolean alreadyLogin = session.getAttribute(SPRING_SECURITY_CONTEXT) != null;
                if (alreadyLogin) {
                    synchronizeUserSession(session);
                    session.setAttribute(LAST_ACCESSED_TIME_ATTR, currentTime);
                    session.setAttribute(LAST_ACCESSED_TIME_WITH_LOGIN_ATTR, true);
                }
            }

        }
    }

    private void synchronizeUserSession(Session session) {
        LOG.debug("synchronizeUserSession({})...", session.getId());
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        String userId = principalNameResolver.resolve(session);
        String ipAddress = httpClientIPAddressResolver.resolve(httpReq);
        String agentId = resolveUserAgentId(httpReq);
        LocalDateTime now = LocalDateTime.now();

        UserSession userSession = userSessionRepo.findOne(session.getId());
        if (userSession == null) {
            userSession = new UserSession();
            userSession.setId(session.getId());
            userSession.setCreateUser(userId);
            userSession.setCreatedDate(now);
        }

        userSession.setUserId(userId);
        userSession.setAgentId(agentId);
        userSession.setIpAddress(ipAddress);
        userSession.setTimeout(sessionTimeout);
        userSession.setUpdatedDate(now);
        userSession.setUpdatedUser(hasText(userId) ? userId : "system");
        userSessionRepo.save(userSession);
    }
}
