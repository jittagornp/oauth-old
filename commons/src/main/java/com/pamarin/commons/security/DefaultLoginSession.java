/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Component
class DefaultLoginSession implements LoginSession {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultLoginSession.class);

    @Override
    public void create(UserSession userSession) {
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(
                userSession,
                "",
                userSession.getAuthorities()
        ));

        SecurityContextHolder.setContext(context);
    }

    @Override
    public boolean wasCreated() {
        try {
            getUserSession();
            return true;
        } catch (AuthenticationException ex) {
            LOG.warn(null, ex);
            return false;
        }
    }

    @Override
    @SuppressWarnings("null")
    public UserSession getUserSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            AuthenticationException.throwByMessage("Please login.");
        }

        if (!(authentication.getPrincipal() instanceof UserSession)) {
            AuthenticationException.throwByMessage("Please login, it's not user session.");
        }

        return (UserSession) authentication.getPrincipal();
    }

}
