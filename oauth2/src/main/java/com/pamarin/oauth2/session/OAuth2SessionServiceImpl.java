/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import com.pamarin.commons.provider.HttpSessionProvider;
import com.pamarin.oauth2.collection.OAuth2AccessToken;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.model.OAuth2Session;
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

    private static final String OAUTH2_SESSION = "OAUTH2_SESSION";

    @Autowired
    private OAuth2SessionBuilderService sessionBuilderService;

    @Autowired
    private HttpSessionProvider sessionProvider;

    private String makeAttributeKey(String attribute) {
        return OAUTH2_SESSION + ":" + attribute;
    }

    @Override
    public OAuth2Session getSessionByOAuth2AccessToken(OAuth2AccessToken accessToken) {
        if (accessToken == null) {
            throw new UnauthorizedClientException("Access token not found.");
        }
        String attributeKey = makeAttributeKey(accessToken.getClientId());
        HttpSession session = sessionProvider.provide();
        OAuth2Session oauth2Session = (OAuth2Session) session.getAttribute(attributeKey);
        if (oauth2Session == null) {
            oauth2Session = sessionBuilderService.build(accessToken);
            session.setAttribute(attributeKey, oauth2Session);
        }

        return oauth2Session;
    }
}
