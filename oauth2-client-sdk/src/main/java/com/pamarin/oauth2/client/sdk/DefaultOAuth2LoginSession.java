/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.exception.AuthenticationException;
import com.pamarin.commons.security.DefaultUserDetails;
import static com.pamarin.oauth2.client.sdk.OAuth2SdkConstant.OAUTH2_SECURITY_CONTEXT;
import static com.pamarin.oauth2.client.sdk.OAuth2SdkConstant.OAUTH2_SESSION;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DefaultOAuth2LoginSession implements OAuth2LoginSession {

    private final OAuth2ClientOperations clientOperations;

    public DefaultOAuth2LoginSession(OAuth2ClientOperations clientOperations) {
        this.clientOperations = clientOperations;
    }

    @Override
    public OAuth2Session login(String accessToken, HttpServletRequest httpReq) {
        if (!hasText(accessToken)) {
            logout(httpReq);
            throw new AuthenticationException("Please login.");
        }

        return doLogin(accessToken, httpReq);
    }

    private OAuth2Session doLogin(String accessToken, HttpServletRequest httpReq) {
        try {
            OAuth2Session session = clientOperations.getSession(accessToken);
            savePrincipal(session, httpReq);
            return session;
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new AuthenticationException("Please login.");
            }
            throw ex;
        }
    }

    @Override
    public void logout(HttpServletRequest httpReq) {
        httpReq.setAttribute(OAUTH2_SESSION, null);
        httpReq.setAttribute(OAUTH2_SECURITY_CONTEXT, null);
    }

    private void savePrincipal(OAuth2Session session, HttpServletRequest httpReq) {
        httpReq.setAttribute(OAUTH2_SESSION, session);
        httpReq.setAttribute(OAUTH2_SECURITY_CONTEXT, convertToSecurityContext(session.getUser()));
        int maxAge = (int) (session.getExpiresAt() - session.getIssuedAt()) / 1000; //convert from milliseconds to seconds
        log.debug("session.maxInactiveInterval => {} seconds", maxAge);
        httpReq.getSession().setMaxInactiveInterval(maxAge);
    }

    private SecurityContext convertToSecurityContext(OAuth2Session.User user) {
        DefaultUserDetails userDetails = DefaultUserDetails.builder()
                .username(user.getId())
                .password(null)
                .authorities(user.getAuthorities())
                .build();

        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        ));
        return context;
    }
}
