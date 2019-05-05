/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContext;
import static org.springframework.security.core.context.SecurityContextHolder.createEmptyContext;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import static org.springframework.security.core.context.SecurityContextHolder.setContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 *
 * @author jitta
 */
public class StatelessSessionSecurityContextRepository implements SecurityContextRepository {

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder holder) {
        HttpServletRequest httpReq = holder.getRequest();
        if (httpReq == null) {
            return createEmptyContext();
        }
        HttpSession session = httpReq.getSession(false);
        if (session == null) {
            return createEmptyContext();
        }
        SecurityContext context = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT);
        if (context == null) {
            return getContext();
        }
        return context;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        if (httpReq != null) {
            HttpSession session = httpReq.getSession(false);
            if (session != null) {
                session.setAttribute(SPRING_SECURITY_CONTEXT, context);
                setContext(context);
            }
        }
    }

    @Override
    public boolean containsContext(HttpServletRequest httpReq) {
        if (httpReq == null) {
            return false;
        }
        HttpSession session = httpReq.getSession(false);
        if (session == null) {
            return false;
        }
        SecurityContext context = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT);
        if (context == null) {
            context = getContext();
        }
        return context != null;
    }

}
