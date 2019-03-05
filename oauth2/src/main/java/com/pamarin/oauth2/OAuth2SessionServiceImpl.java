/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.constant.OAuth2Constant;
import com.pamarin.oauth2.domain.OAuth2Approval;
import com.pamarin.oauth2.domain.OAuth2Client;
import com.pamarin.oauth2.domain.UserSession;
import com.pamarin.oauth2.exception.OAuth2ClientNotFoundException;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.model.OAuth2Session;
import com.pamarin.oauth2.repository.OAuth2ApprovalRepo;
import com.pamarin.oauth2.repository.OAuth2ClientRepo;
import com.pamarin.oauth2.repository.OAuth2ClientScopeRepo;
import com.pamarin.oauth2.repository.UserSessionRepo;
import com.pamarin.oauth2.service.AccessTokenVerification;
import com.pamarin.oauth2.service.OAuth2SessionService;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jitta
 */
@Service
@Transactional
public class OAuth2SessionServiceImpl implements OAuth2SessionService {

    private static final String OAUTH2_SESSION = "oauth2-session";

    private static final String LAST_ACCESSED_USER_SESSION = "lastAccessedTimeUserSession";

    private static final long USER_SESSION_SYNCRONIZE_TIMEOUT = 1000 * 30; //30 seconds

    @Autowired
    private OAuth2ClientScopeRepo clientScopeRepo;

    @Autowired
    private OAuth2ClientRepo clientRepo;

    @Autowired
    private OAuth2ApprovalRepo approvalRepo;

    @Autowired
    private LoginSession loginSession;

    @Autowired
    private UserSessionRepo userSessionRepo;

    private OAuth2Session buildOAuth2Session(AccessTokenVerification.Output output) {
        OAuth2Approval approval = approvalRepo.findOne(new OAuth2Approval.PK(output.getUserId(), output.getClientId()));
        if (approval == null) {
            throw new UnauthorizedClientException("Unauthorized client " + output.getClientId() + " for user " + output.getUserId() + ".");
        }

        OAuth2Client client = clientRepo.findOne(output.getClientId());
        if (client == null) {
            throw new OAuth2ClientNotFoundException("Not found client id " + output.getClientId());
        }
        return OAuth2Session.builder()
                .id(output.getSessionId())
                .issuedAt(output.getIssuedAt())
                .expiresAt(output.getExpiresAt())
                .user(
                        OAuth2Session.User.builder()
                                .id(output.getUserId())
                                .name("นาย สมชาย ใจดี")
                                .authorities(Arrays.asList("sso"))
                                .build()
                )
                .client(OAuth2Session.Client.builder()
                        .id(client.getId())
                        .name(client.getName())
                        .scopes(clientScopeRepo.findScopeByClientId(client.getId()))
                        .build()
                )
                .build();
    }

    private String makeAttributeKey(String attribute) {
        return OAUTH2_SESSION + ":" + attribute;
    }

    @Override
    public OAuth2Session getSession(HttpServletRequest request) {

        loginSession.getUserDetails();

        AccessTokenVerification.Output accessToken = (AccessTokenVerification.Output) request.getAttribute(OAuth2Constant.ACCESS_TOKEN_ATTRIBUTE);
        if (accessToken == null) {
            throw new UnauthorizedClientException("Access token not found.");
        }

        String attributeKey = makeAttributeKey(accessToken.getClientId());
        HttpSession session = request.getSession();
        OAuth2Session oauth2Session = (OAuth2Session) session.getAttribute(attributeKey);
        if (oauth2Session == null) {
            oauth2Session = buildOAuth2Session(accessToken);
            session.setAttribute(attributeKey, oauth2Session);
        }

        Long lastAcccessedTime = (Long) session.getAttribute(LAST_ACCESSED_USER_SESSION);
        if (lastAcccessedTime == null) {
            updateUserSession(oauth2Session);
            session.setAttribute(LAST_ACCESSED_USER_SESSION, System.currentTimeMillis());
        } else {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastAcccessedTime > USER_SESSION_SYNCRONIZE_TIMEOUT) {
                updateUserSession(oauth2Session);
                session.setAttribute(LAST_ACCESSED_USER_SESSION, currentTime);
            }
        }

        return oauth2Session;
    }

    private void updateUserSession(OAuth2Session oauth2Session) {
        UserSession userSession = userSessionRepo.findOne(oauth2Session.getId());
        if (userSession == null) {
            userSession = new UserSession();
            userSession.setId(oauth2Session.getId());
            userSession.setUserId(oauth2Session.getUser().getId());
            userSessionRepo.save(userSession);
        } else {
            userSession.setId(oauth2Session.getId());
            userSession.setUserId(oauth2Session.getUser().getId());
        }
    }

}
