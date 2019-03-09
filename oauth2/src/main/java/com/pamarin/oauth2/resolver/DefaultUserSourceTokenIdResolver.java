/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.resolver;

import java.util.Base64;
import java.util.stream.Stream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import static org.apache.commons.lang.ArrayUtils.isEmpty;

/**
 *
 * @author jitta
 */
public class DefaultUserSourceTokenIdResolver implements UserSourceTokenIdResolver {

    private final String cookieName;

    public DefaultUserSourceTokenIdResolver(String cookieName) {
        this.cookieName = cookieName;
    }

    @Override
    public String resolve(HttpServletRequest httpReq) {
        Cookie[] cookies = httpReq.getCookies();
        if (isEmpty(cookies)) {
            return null;
        }

        return Stream.of(cookies)
                .filter(cookie -> cookie != null && cookieName.equalsIgnoreCase(cookie.getName()))
                .map(cookie -> decode(cookie.getValue()))
                .findFirst()
                .orElse(null);
    }

    private String decode(String cookieValue) {
        return new String(Base64.getDecoder().decode(cookieValue));
    }

}
