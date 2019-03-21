/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.commons.provider.HttpSessionProvider;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.domain.LoginHistory;
import com.pamarin.oauth2.repository.LoginHistoryRepo;
import com.pamarin.oauth2.resolver.UserAgentTokenIdResolver;
import com.pamarin.oauth2.service.LoginHistoryService;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Service
@Transactional
public class LoginHistoryServiceImpl implements LoginHistoryService {

    private static final String LOGIN_HISTORY_ATTR = "loginHistory";

    @Autowired
    private LoginSession loginSession;

    @Autowired
    private HttpServletRequestProvider httpServletRequestProvider;

    @Autowired
    private HttpClientIPAddressResolver httpClientIPAddressResolver;

    @Autowired
    private UserAgentTokenIdResolver userAgentTokenIdResolver;

    @Autowired
    private LoginHistoryRepo loginHistoryRepo;

    @Override
    public void createHistory() {

        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        HttpSession session = httpReq.getSession();

        LoginHistory history = new LoginHistory();
        String id = UUID.randomUUID().toString();
        history.setId(id);
        history.setIpAddress(httpClientIPAddressResolver.resolve(httpReq));
        history.setLoginDate(LocalDateTime.now());
        history.setAgentId(userAgentTokenIdResolver.resolve(httpReq));
        history.setSessionId(loginSession.getSessionId());
        history.setUserId(loginSession.getUserDetails().getUsername());
        loginHistoryRepo.save(history);

        session.setAttribute(LOGIN_HISTORY_ATTR, id);
    }

    @Override
    public void stampLogout() {
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        HttpSession session = httpReq.getSession();
        String id = (String) session.getAttribute(LOGIN_HISTORY_ATTR);
        if (hasText(id)) {
            LoginHistory history = loginHistoryRepo.findOne(id);
            if (history != null) {
                history.setLogoutDate(LocalDateTime.now());
            }
        }
    }

}
