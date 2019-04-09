/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

import java.util.stream.Stream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import static org.apache.commons.lang.ArrayUtils.isEmpty;
import org.springframework.util.Assert;

/**
 *
 * @author jitta
 */
public class DefaultHttpCookieResolver implements HttpCookieResolver {

    private final String cookieName;

    public DefaultHttpCookieResolver(String cookieName) {
        this.cookieName = cookieName;
        Assert.notNull(cookieName, "require cookieName.");
    }

    @Override
    public String resolve(HttpServletRequest httpReq) {
        Assert.notNull(httpReq, "require httpReq.");
        Cookie[] cookies = httpReq.getCookies();
        if (isEmpty(cookies)) {
            return null;
        }

        return Stream.of(cookies)
                .filter(cookie -> cookie != null && cookieName.equalsIgnoreCase(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
