/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.exception.AuthenticationException;
import com.pamarin.commons.security.DefaultUserDetails;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.client.HttpClientErrorException;

/**
 *
 * @author jitta
 */
public class DefaultOAuth2LoginSession implements OAuth2LoginSession {

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    private final OAuth2ClientOperations clientOperations;

    public DefaultOAuth2LoginSession(OAuth2ClientOperations clientOperations) {
        this.clientOperations = clientOperations;
    }

    @Override
    public void login(String accessToken, HttpServletRequest httpReq) {
        if (!hasText(accessToken)) {
            logout(httpReq);
            throw new AuthenticationException("Please login");
        }

        doLogin(accessToken, httpReq);
    }

    private void doLogin(String accessToken, HttpServletRequest httpReq) {
        try {
            OAuth2Session session = clientOperations.getSession(accessToken);
            setPrincipal(session, httpReq);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new AuthenticationException("Please login");
            }
            throw ex;
        }
    }

    @Override
    public void logout(HttpServletRequest httpReq) {
        HttpSession session = httpReq.getSession(false);
        if (session != null) {
            session.setAttribute(SPRING_SECURITY_CONTEXT, null);
        }
    }

    private void setPrincipal(OAuth2Session session, HttpServletRequest httpReq) {
        HttpSession httpSession = httpReq.getSession(true);
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT, buildSecurityContext(session.getUser()));
        OAuth2SessionContext.setSession(session);
    }

    private SecurityContext buildSecurityContext(OAuth2Session.User user) {
        DefaultUserDetails userDetails = DefaultUserDetails.builder()
                .username(user.getId())
                .password(null)
                .authorities(user.getAuthorities())
                .build();

        SecurityContext context = new SecurityContextImpl();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        context.setAuthentication(token);
        return context;
    }
}
