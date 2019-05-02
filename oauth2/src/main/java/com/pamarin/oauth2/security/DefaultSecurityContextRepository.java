/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContext;
import static org.springframework.security.core.context.SecurityContextHolder.createEmptyContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 *
 * @author jitta
 */
public class DefaultSecurityContextRepository implements SecurityContextRepository {

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
            return createEmptyContext();
        }
        return context;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        if (httpReq != null) {
            HttpSession session = httpReq.getSession(false);
            if (session != null) {
                session.setAttribute(SPRING_SECURITY_CONTEXT, context);
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
        return (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT) != null;
    }

}
