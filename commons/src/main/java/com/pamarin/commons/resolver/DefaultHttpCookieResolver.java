/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

import java.util.stream.Stream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import static org.apache.commons.lang.ArrayUtils.isEmpty;
import org.springframework.util.Assert;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
public class DefaultHttpCookieResolver implements HttpCookieResolver {

    private final String cachedKey;

    private final String cookieName;

    public DefaultHttpCookieResolver(String cookieName) {
        Assert.notNull(cookieName, "require cookieName.");
        this.cookieName = cookieName;
        this.cachedKey = HttpCookieResolver.class.getName() + "." + cookieName;
    }

    @Override
    public String resolve(HttpServletRequest httpReq) {
        Assert.notNull(httpReq, "require httpReq.");
        String cached = (String) httpReq.getAttribute(cachedKey);
        if (hasText(cached)) {
            return cached;
        }

        Cookie[] cookies = httpReq.getCookies();
        if (isEmpty(cookies)) {
            return null;
        }

        return cached(Stream.of(cookies)
                .filter(cookie -> cookie != null && cookieName.equalsIgnoreCase(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null), httpReq);
    }

    private String cached(String cookieValue, HttpServletRequest httpReq) {
        httpReq.setAttribute(cachedKey, cookieValue);
        return cookieValue;
    }
}
