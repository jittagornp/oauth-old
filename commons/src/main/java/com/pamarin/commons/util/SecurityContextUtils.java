/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

/**
 *
 * @author jitta
 */
public class SecurityContextUtils {

    private SecurityContextUtils() {

    }

    public static String getAuthenticationName(SecurityContext context) {
        if (context == null) {
            return null;
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }

}
