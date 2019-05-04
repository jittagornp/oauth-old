/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.exception.AuthenticationException;
import com.pamarin.commons.provider.HttpSessionProvider;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import static org.springframework.security.core.context.SecurityContextHolder.clearContext;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import static org.springframework.security.core.context.SecurityContextHolder.setContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Slf4j
@Component
class DefaultLoginSession implements LoginSession {

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    private final HttpSessionProvider httpSessionProvider;

    @Autowired
    public DefaultLoginSession(HttpSessionProvider httpSessionProvider) {
        this.httpSessionProvider = httpSessionProvider;
    }

    @Override
    public void create(UserDetails userDetails) {
        SecurityContext context = new SecurityContextImpl();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        context.setAuthentication(token);
        setContext(context);
        setSessionContext(context);
    }

    @Override
    public boolean wasCreated() {
        try {
            getUserDetails();
            return true;
        } catch (Exception ex) {
            log.warn(null, ex);
            return false;
        }
    }

    @Override
    public UserDetails getUserDetails() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            throw new AuthenticationException("Please login, principal is null.");
        }

        if (!(principal instanceof UserDetails)) {
            log.debug("principal class name => {}", principal.getClass().getName());
            log.debug("principal value => {}", principal);
            throw new AuthenticationException("Please login, principal is not user details.");
        }

        return (UserDetails) principal;
    }

    @Override
    public Authentication getAuthentication() {
        SecurityContext context = getSessionContext();
        if (context == null) {
            context = getContext();
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            throw new AuthenticationException("Please login, authentication is null.");
        }

        return authentication;
    }

    @Override
    public String getSessionId() {
        HttpSession session = httpSessionProvider.provide();
        return session == null ? null : session.getId();
    }

    @Override
    public void logout() {
        HttpSession session = httpSessionProvider.provide();
        if (session != null) {
            session.setMaxInactiveInterval(0);
            session.invalidate();
        }
        clearContext();
        setSessionContext(null);
    }

    private SecurityContext getSessionContext() {
        HttpSession session = httpSessionProvider.provide();
        if (session != null) {
            return (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT);
        }
        return null;
    }

    private void setSessionContext(SecurityContext context) {
        HttpSession session = httpSessionProvider.provide();
        if (session != null) {
            session.setAttribute(SPRING_SECURITY_CONTEXT, context);
        }
    }
}
