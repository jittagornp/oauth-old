/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.exception.AuthenticationException;
import com.pamarin.commons.provider.HttpSessionProvider;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Component
class DefaultLoginSession implements LoginSession {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultLoginSession.class);

    @Autowired
    private HttpSessionProvider httpSessionProvider;

    @Override
    public void create(UserDetails userDetails) {
        SecurityContext context = new SecurityContextImpl();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        context.setAuthentication(token);
        SecurityContextHolder.clearContext();
        SecurityContextHolder.setContext(context);
    }

    @Override
    public boolean wasCreated() {
        try {
            getUserDetails();
            return true;
        } catch (AuthenticationException ex) {
            LOG.warn(null, ex);
            return false;
        }
    }

    @Override
    @SuppressWarnings("null")
    public UserDetails getUserDetails() {
        Authentication authentication = getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            LOG.debug("principal class name => {}", authentication.getPrincipal().getClass().getName());
            LOG.debug("principal value => {}", authentication.getPrincipal());
            AuthenticationException.throwByMessage("Please login, it's not user details.");
        }

        return (UserDetails) authentication.getPrincipal();
    }

    @Override
    @SuppressWarnings("null")
    public Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            AuthenticationException.throwByMessage("Please login.");
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            AuthenticationException.throwByMessage("Please login.");
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
        SecurityContextHolder.clearContext();
    }

}
