/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.cache.OAuth2SessionCacheStore;
import com.pamarin.oauth2.domain.OAuth2Client;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.model.OAuth2Session;
import com.pamarin.oauth2.repository.OAuth2ClientRepo;
import com.pamarin.oauth2.repository.OAuth2ClientScopeRepo;
import com.pamarin.oauth2.service.AccessTokenVerification;
import com.pamarin.oauth2.service.OAuth2SessionService;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(OAuth2SessionServiceImpl.class);

    @Autowired
    private OAuth2ClientScopeRepo clientScopeRepo;

    @Autowired
    private OAuth2ClientRepo clientRepo;

    @Autowired
    private AccessTokenVerification accessTokenVerification;

    @Autowired
    private OAuth2SessionCacheStore cacheStore;

    @Autowired
    private LoginSession loginSession;

    private OAuth2Session buildOAuth2Session(AccessTokenVerification.Output output) {
        OAuth2Client client = clientRepo.findOne(output.getClientId());
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
                        .id(client == null ? null : client.getId())
                        .name(client == null ? null : client.getName())
                        .scopes(client == null ? null : clientScopeRepo.findScopeByClientId(client.getId()))
                        .build()
                )
                .token(output.getId())
                .build();
    }

    @Override
    public OAuth2Session getSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null) {
            throw new UnauthorizedClientException("Session it's not create.");
        }

        AccessTokenVerification.Output accessToken = (AccessTokenVerification.Output) request.getAttribute("accessToken");
        if (accessToken == null) {
            throw new UnauthorizedClientException("Access token not found.");
        }

        OAuth2Session oauth2Session = (OAuth2Session) session.getAttribute("clientSession:" + accessToken.getClientId());
        if (oauth2Session == null) {
            oauth2Session = buildOAuth2Session(accessToken);
            session.setAttribute("clientSession:" + accessToken.getClientId(), oauth2Session);
        }

        LOG.debug("username => {}", loginSession.getUserDetails().getUsername());
        LOG.debug("sessionId => {}", loginSession.getSessionId());
        return oauth2Session;
    }

}
