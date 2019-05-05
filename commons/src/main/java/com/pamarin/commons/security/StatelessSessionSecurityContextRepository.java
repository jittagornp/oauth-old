/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import static com.pamarin.commons.util.SecurityContextUtils.getAuthenticationName;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import static org.springframework.security.core.context.SecurityContextHolder.createEmptyContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 *
 * @author jitta
 */
@Slf4j
public class StatelessSessionSecurityContextRepository implements SecurityContextRepository {

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder holder) {
        if (holder == null) {
            return createEmptyContext();
        }
        HttpServletRequest httpReq = holder.getRequest();
        if (httpReq == null) {
            return createEmptyContext();
        }
        HttpSession session = httpReq.getSession(false);
        if (session == null) {
            return createEmptyContext();
        }
        Object obj = session.getAttribute(SPRING_SECURITY_CONTEXT);
        if (obj == null) {
            return createEmptyContext();
        }
        if (!(obj instanceof SecurityContext)) {
            return createEmptyContext();
        }
        return (SecurityContext) obj;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        if (httpReq != null) {
            HttpSession session = httpReq.getSession(false);
            if (session != null) {
                session.setAttribute(SPRING_SECURITY_CONTEXT, context);
                log.debug("save SecurityContext of => {}", getAuthenticationName(context));
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
        Object obj = session.getAttribute(SPRING_SECURITY_CONTEXT);
        if (obj == null) {
            return false;
        }
        return obj instanceof SecurityContext;
    }
}
