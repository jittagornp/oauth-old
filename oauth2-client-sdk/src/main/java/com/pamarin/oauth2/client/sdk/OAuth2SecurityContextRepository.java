/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import static com.pamarin.oauth2.client.sdk.OAuth2SdkConstant.OAUTH2_SECURITY_CONTEXT;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import static org.springframework.security.core.context.SecurityContextHolder.createEmptyContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 *
 * @author jitta
 */
public class OAuth2SecurityContextRepository implements SecurityContextRepository {

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder holder) {
        HttpServletRequest httpReq = holder.getRequest();
        if (httpReq == null) {
            return createEmptyContext();
        }
        SecurityContext context = (SecurityContext) httpReq.getAttribute(OAUTH2_SECURITY_CONTEXT);
        if (context == null) {
            return createEmptyContext();
        }
        return context;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        if (httpReq != null) {
            httpReq.setAttribute(OAUTH2_SECURITY_CONTEXT, context);
        }
    }

    @Override
    public boolean containsContext(HttpServletRequest httpReq) {
        if (httpReq == null) {
            return false;
        }
        return (SecurityContext) httpReq.getAttribute(OAUTH2_SECURITY_CONTEXT) != null;
    }

}
