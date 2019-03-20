/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.oauth2.domain.UserSession;
import com.pamarin.oauth2.domain.UserAgentEntity;
import com.pamarin.oauth2.repository.UserSessionRepo;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import static org.springframework.util.StringUtils.hasText;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import com.pamarin.commons.resolver.PrincipalNameResolver;
import com.pamarin.commons.resolver.UserAgent;
import com.pamarin.commons.resolver.UserAgentResolver;
import com.pamarin.oauth2.converter.UserAgentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.session.Session;
import com.pamarin.oauth2.repository.UserAgentRepo;
import com.pamarin.oauth2.resolver.UserAgentTokenIdResolver;
import com.pamarin.oauth2.service.DatabaseSessionRepo;

/**
 *
 * @author jitta
 */
public class DatabaseSessionRepoImpl implements DatabaseSessionRepo {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseSessionRepoImpl.class);

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    private static final String LAST_ACCESSED_TIME_ATTR = "lastAccessedTime";

    private static final String LAST_ACCESSED_TIME_WITH_LOGIN_ATTR = "lastAccessedTimeWithLogin";

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

    @Autowired
    private UserAgentResolver userAgentResolver;

    @Autowired
    private UserAgentConverter userAgentConverter;

    public DatabaseSessionRepoImpl(Integer sessionTimeout, Long synchronizeTimeout) {
        this.sessionTimeout = sessionTimeout;
        this.synchronizeTimeout = synchronizeTimeout;
    }

    private String resolveUserAgentId(HttpServletRequest httpReq, boolean extractUserAgent) {
        String agentId = userAgentTokenIdResolver.resolve(httpReq);
        if (!hasText(agentId)) {
            return null;
        }

        if (extractUserAgent) {
            UserAgent userAgent = userAgentResolver.resolve(httpReq);
            if (userAgent != null) {
                UserAgentEntity entity = userAgentConverter.convert(userAgent);
                entity.setId(agentId);
                userAgentRepo.save(entity);
            }
        } else {
            if (!userAgentRepo.exists(agentId)) {
                UserAgentEntity entity = new UserAgentEntity();
                entity.setId(agentId);
                userAgentRepo.save(entity);
            }
        }

        return agentId;
    }

    @Override
    public void synchronize(Session session) {
        long currentTime = System.currentTimeMillis();
        Long lastAcccessedTime = (Long) session.getAttribute(LAST_ACCESSED_TIME_ATTR);
        if (lastAcccessedTime == null) {
            synchronize(session, true);
            session.setAttribute(LAST_ACCESSED_TIME_ATTR, currentTime);
        } else if (currentTime - lastAcccessedTime > synchronizeTimeout) {
            synchronize(session, false);
            session.setAttribute(LAST_ACCESSED_TIME_ATTR, currentTime);
        } else {
            Object firstTimeWithLogin = session.getAttribute(LAST_ACCESSED_TIME_WITH_LOGIN_ATTR);
            if (firstTimeWithLogin == null) {
                boolean alreadyLogin = session.getAttribute(SPRING_SECURITY_CONTEXT) != null;
                if (alreadyLogin) {
                    synchronize(session, true);
                    session.setAttribute(LAST_ACCESSED_TIME_ATTR, currentTime);
                    session.setAttribute(LAST_ACCESSED_TIME_WITH_LOGIN_ATTR, currentTime);
                }
            }
        }
    }

    private void synchronize(Session session, boolean extractUserAgent) {
        LOG.debug("synchronizeUserSession({})...", session.getId());
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        String userId = principalNameResolver.resolve(session);
        String ipAddress = httpClientIPAddressResolver.resolve(httpReq);
        String agentId = resolveUserAgentId(httpReq, extractUserAgent);
        LocalDateTime now = LocalDateTime.now();
        String updatedUser = hasText(userId) ? userId : "system";

        UserSession userSession = userSessionRepo.findOne(session.getId());
        if (userSession == null) {
            userSession = new UserSession();
            userSession.setId(session.getId());
            userSession.setCreateUser(updatedUser);
            userSession.setCreatedDate(now);
        }

        userSession.setUserId(userId);
        userSession.setAgentId(agentId);
        userSession.setIpAddress(ipAddress);
        userSession.setTimeout(sessionTimeout);
        userSession.setUpdatedDate(now);
        userSession.setUpdatedUser(updatedUser);
        userSessionRepo.save(userSession);
    }
}
