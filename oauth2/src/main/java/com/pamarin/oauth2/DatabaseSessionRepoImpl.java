/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.generator.PrimaryKeyGenerator;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.session.Session;
import com.pamarin.oauth2.repository.UserAgentRepo;
import com.pamarin.oauth2.resolver.UserAgentTokenIdResolver;
import com.pamarin.oauth2.repository.DatabaseSessionRepo;
import org.springframework.beans.BeanUtils;

/**
 *
 * @author jitta
 */
public class DatabaseSessionRepoImpl implements DatabaseSessionRepo {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseSessionRepoImpl.class);

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    private static final String LAST_ACCESSED_TIME_ATTR = "lastAccessedTime";

    private static final String LAST_ACCESSED_TIME_WITH_LOGIN_ATTR = "lastAccessedTimeWithLogin";

    private final Integer synchronizeTimeout;

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
    private PrimaryKeyGenerator primaryKeyGenerator; 

    public DatabaseSessionRepoImpl(Integer sessionTimeout, Integer synchronizeTimeout) {
        this.sessionTimeout = sessionTimeout;
        this.synchronizeTimeout = synchronizeTimeout;
    }

    @Override
    public void synchronize(Session session) {
        long currentTime = System.currentTimeMillis();
        Long lastAcccessedTime = (Long) session.getAttribute(LAST_ACCESSED_TIME_ATTR);
        if (lastAcccessedTime == null) {
            updateSession(session, false);
            session.setAttribute(LAST_ACCESSED_TIME_ATTR, currentTime);
        } else if (currentTime - lastAcccessedTime > synchronizeTimeout) {
            updateSession(session, false);
            session.setAttribute(LAST_ACCESSED_TIME_ATTR, currentTime);
        } else {
            Object firstTimeWithLogin = session.getAttribute(LAST_ACCESSED_TIME_WITH_LOGIN_ATTR);
            if (firstTimeWithLogin == null) {
                boolean alreadyLogin = session.getAttribute(SPRING_SECURITY_CONTEXT) != null;
                if (alreadyLogin) {
                    updateSession(session, true);
                    session.setAttribute(LAST_ACCESSED_TIME_ATTR, currentTime);
                    session.setAttribute(LAST_ACCESSED_TIME_WITH_LOGIN_ATTR, currentTime);
                }
            }
        }
    }

    private void updateSession(Session session, boolean extractUserAgent) {

        LOG.debug("updateSession({}, {})...", session.getId(), extractUserAgent);

        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        String userId = principalNameResolver.resolve(session);
        String ipAddress = httpClientIPAddressResolver.resolve(httpReq);
        String agentId = resolveUserAgent(httpReq, extractUserAgent);
        long now = System.currentTimeMillis();

        UserSession userSession = userSessionRepo.findBySessionId(session.getId());
        if (userSession == null) {
            userSession = new UserSession();
            userSession.setId(primaryKeyGenerator.generate());
            userSession.setSessionId(session.getId());
            userSession.setCreationTime(now);
        }

        userSession.setUserId(userId);
        if (hasText(agentId)) { //Not update if don't have agentId
            userSession.setAgentId(agentId);
        }
        userSession.setIpAddress(ipAddress);
        userSession.setMaxInactiveInterval(sessionTimeout);
        userSession.setLastAccessedTime(now);
        userSessionRepo.save(userSession);
    }

    private String resolveUserAgent(HttpServletRequest httpReq, boolean extractUserAgent) {
        String agentId = userAgentTokenIdResolver.resolve(httpReq);
        if (!hasText(agentId)) {
            return null;
        }

        if (!userAgentRepo.exists(agentId)) {
            insertNewUserAgent(agentId, httpReq, extractUserAgent);
            return agentId;
        }

        if (extractUserAgent) {
            updateOldUserAgent(agentId, httpReq);
        }
        return agentId;
    }

    private void insertNewUserAgent(String agentId, HttpServletRequest httpReq, boolean extractUserAgent) {
        LocalDateTime now = LocalDateTime.now();
        UserAgentEntity entity = new UserAgentEntity();
        entity.setId(agentId);
        entity.setCreatedDate(now);
        entity.setUpdatedDate(now);
        if (extractUserAgent) {
            extractUserAgentHeader(httpReq, entity);
        }
        userAgentRepo.save(entity);
    }

    private void updateOldUserAgent(String agentId, HttpServletRequest httpReq) {
        LocalDateTime now = LocalDateTime.now();
        UserAgentEntity entity = userAgentRepo.findOne(agentId);
        extractUserAgentHeader(httpReq, entity);
        entity.setUpdatedDate(now);
        userAgentRepo.save(entity);
    }

    private void extractUserAgentHeader(HttpServletRequest httpReq, UserAgentEntity entity) {
        UserAgent userAgent = userAgentResolver.resolve(httpReq);
        if (userAgent != null) {
            BeanUtils.copyProperties(userAgent, entity);
        }
    }
}