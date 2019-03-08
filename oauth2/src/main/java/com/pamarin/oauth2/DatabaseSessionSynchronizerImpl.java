/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.domain.UserSession;
import com.pamarin.oauth2.domain.UserSource;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.repository.UserSessionRepo;
import com.pamarin.oauth2.repository.UserSourceRepo;
import com.pamarin.oauth2.resolver.UserSourceTokenIdResolver;
import java.time.LocalDateTime;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.pamarin.oauth2.service.DatabaseSessionSynchronizer;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import static org.springframework.util.StringUtils.hasText;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jitta
 */
@Service
@Transactional 
public class DatabaseSessionSynchronizerImpl implements DatabaseSessionSynchronizer {

    private static final String LAST_ACCESSED_TIME = "lastAccessedTimeUserSession";

    private static final long SYNCHRONIZE_TIMEOUT = 1000 * 30; //30 seconds

    @Value("${spring.session.timeout}")
    private Integer sessionTimeout;

    @Autowired
    private HttpServletRequestProvider httpServletRequestProvider;

    @Autowired
    private UserSessionRepo userSessionRepo;

    @Autowired
    private UserSourceRepo userSourceRepo;

    @Autowired
    private LoginSession loginSession;

    @Autowired
    private UserSourceTokenIdResolver userSourceTokenIdResolver;

    @Autowired
    private HttpClientIPAddressResolver httpClientIPAddressResolver;

    @Override
    public void createSession() {
        UserDetails userDetails = loginSession.getUserDetails();
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        HttpSession session = httpReq.getSession();

        String sessionId = session.getId();
        String userId = userDetails.getUsername();

        UserSession userSession = new UserSession();
        userSession.setId(sessionId);
        userSession.setUserId(userId);
        userSession.setTimeout(sessionTimeout);
        userSession.setSourceId(resolveUserSourceId(httpReq));
        userSession.setIpAddress(httpClientIPAddressResolver.resolve(httpReq));
        userSessionRepo.save(userSession);

        session.setAttribute(LAST_ACCESSED_TIME, System.currentTimeMillis());

    }

    private String resolveUserSourceId(HttpServletRequest httpReq) {
        String sourceId = userSourceTokenIdResolver.resolve(httpReq);
        if (!hasText(sourceId)) {
            throw new IllegalStateException("user-source token is null.");
        }

        UserSource userSource = userSourceRepo.findOne(sourceId);
        if (userSource == null) {
            userSource = new UserSource();
            userSource.setId(sourceId);
            userSourceRepo.save(userSource);
        }
        return userSource.getId();
    }

    @Override
    public void synchronize() {
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        HttpSession session = httpReq.getSession();
        Long lastAcccessedTime = (Long) session.getAttribute(LAST_ACCESSED_TIME);
        if (lastAcccessedTime == null) {
            throw new UnauthorizedClientException("session." + LAST_ACCESSED_TIME + " is null");
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAcccessedTime > SYNCHRONIZE_TIMEOUT) {
            UserDetails userDetails = loginSession.getUserDetails();
            String sessionId = session.getId();
            String userId = userDetails.getUsername();

            userSessionRepo.update(
                    sessionId,
                    LocalDateTime.now(),
                    userId,
                    httpClientIPAddressResolver.resolve(httpReq)
            );

            session.setAttribute(LAST_ACCESSED_TIME, currentTime);
        }
    }

}
