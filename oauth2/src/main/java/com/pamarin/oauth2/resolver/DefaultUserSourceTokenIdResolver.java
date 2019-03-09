/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.resolver;

import com.pamarin.commons.resolver.DefaultHttpCookieResolver;
import com.pamarin.commons.resolver.HttpCookieResolver;
import com.pamarin.commons.util.Base64Utils;
import javax.servlet.http.HttpServletRequest;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
public class DefaultUserSourceTokenIdResolver implements UserSourceTokenIdResolver {

    private final HttpCookieResolver httpCookieResolver;

    public DefaultUserSourceTokenIdResolver(String cookieName) {
        httpCookieResolver = new DefaultHttpCookieResolver(cookieName);
    }

    @Override
    public String resolve(HttpServletRequest httpReq) {
        String cookieValue = httpCookieResolver.resolve(httpReq);
        if (!hasText(cookieValue)) {
            return null;
        }
        return Base64Utils.decode(cookieValue);
    }
}
