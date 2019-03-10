/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.constant.OAuth2Constant;
import com.pamarin.oauth2.domain.OAuth2AccessToken;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.model.OAuth2Session;
import com.pamarin.oauth2.service.OAuth2SessionService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pamarin.oauth2.service.DatabaseSessionSynchronizer;
import com.pamarin.oauth2.service.OAuth2SessionBuilderService;

/**
 *
 * @author jitta
 */
@Service
@Transactional
public class OAuth2SessionServiceImpl implements OAuth2SessionService {

    private static final String OAUTH2_SESSION = "oauth2-session";

    @Autowired
    private DatabaseSessionSynchronizer databaseSessionSynchronizer;

    @Autowired
    private OAuth2SessionBuilderService sessionBuilderService;

    private String makeAttributeKey(String attribute) {
        return OAUTH2_SESSION + ":" + attribute;
    }

    @Override
    public OAuth2Session getSession(HttpServletRequest httpReq) {

        databaseSessionSynchronizer.synchronize();

        OAuth2AccessToken accessToken = getAccessToken(httpReq);
        if (accessToken == null) {
            throw new UnauthorizedClientException("Access token not found.");
        }

        String attributeKey = makeAttributeKey(accessToken.getClientId());
        HttpSession session = httpReq.getSession();
        OAuth2Session oauth2Session = (OAuth2Session) session.getAttribute(attributeKey);
        if (oauth2Session == null) {
            oauth2Session = sessionBuilderService.build(accessToken);
            session.setAttribute(attributeKey, oauth2Session);
        }

        return oauth2Session;
    }

    private OAuth2AccessToken getAccessToken(HttpServletRequest httpReq) {
        return (OAuth2AccessToken) httpReq.getAttribute(OAuth2Constant.ACCESS_TOKEN_ATTRIBUTE);
    }
}
