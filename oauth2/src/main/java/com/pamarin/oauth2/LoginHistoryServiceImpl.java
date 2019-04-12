/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.collection.LoginHistory;
import com.pamarin.oauth2.resolver.UserAgentTokenIdResolver;
import com.pamarin.oauth2.service.LoginHistoryService;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.util.StringUtils.hasText;
import com.pamarin.commons.generator.IdGenerator;
import com.pamarin.oauth2.repository.mongodb.LoginHistoryRepository;

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
    private LoginHistoryRepository loginHistoryRepository;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public void createHistory() {
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        LoginHistory history = new LoginHistory();
        String id = idGenerator.generate();
        history.setId(id);
        history.setIpAddress(httpClientIPAddressResolver.resolve(httpReq));
        history.setLoginDate(LocalDateTime.now());
        history.setAgentId(userAgentTokenIdResolver.resolve(httpReq));
        history.setSessionId(loginSession.getSessionId());
        history.setUserId(loginSession.getUserDetails().getUsername());
        loginHistoryRepository.save(history);

        httpReq.getSession().setAttribute(LOGIN_HISTORY_ATTR, id);
    }

    @Override
    public void stampLogout() {
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        String id = (String) httpReq.getSession().getAttribute(LOGIN_HISTORY_ATTR);
        if (hasText(id)) {
            LoginHistory history = loginHistoryRepository.findOne(id);
            if (history != null) {
                history.setLogoutDate(LocalDateTime.now());
                loginHistoryRepository.save(history);
            }
        }
    }

}
