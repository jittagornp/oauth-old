/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.util.Base64Utils;
import java.security.SecureRandom;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
public class DefaultOAuth2AuthorizationState implements OAuth2AuthorizationState {

    private static final String OAUTH2_AUTHORIZATION_STATE = "OAUTH2_AUTHORIZATION_STATE";

    private static final int STATE_SIZE = 11;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String create(HttpServletRequest httpReq) {
        String state = randomState();
        HttpSession session = httpReq.getSession(true);
        if (session != null) {
            session.setAttribute(OAUTH2_AUTHORIZATION_STATE, state);
        }
        return state;
    }

    @Override
    public void verify(HttpServletRequest httpReq) {
        String state = httpReq.getParameter("state");
        if (!hasText(state)) {
            return;
        }
        HttpSession session = httpReq.getSession(false);
        if (session == null) {
            throw new InvalidAuthorizationStateException("session");
        }
        String sessionState = (String) session.getAttribute(OAUTH2_AUTHORIZATION_STATE);
        if (!Objects.equals(state, sessionState)) {
            throw new InvalidAuthorizationStateException(state);
        }
        session.removeAttribute(OAUTH2_AUTHORIZATION_STATE);
    }

    private String randomState() {
        byte[] bytes = new byte[STATE_SIZE];
        secureRandom.nextBytes(bytes);
        return Base64Utils.encode(bytes);
    }
}
