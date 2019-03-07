/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.provider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author jitta
 */
@Component
public class DefaultHttpSessionProvider implements HttpSessionProvider {

    @Override
    public HttpSession provide() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        if (request == null) {
            return null;
        }
        return request.getSession(false);
    }

}
