/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.resolver;

import com.pamarin.commons.resolver.DefaultHttpCookieResolver;
import com.pamarin.commons.resolver.HttpCookieResolver;
import com.pamarin.commons.util.Base64Utils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
public class DefaultUserAgentTokenIdResolver implements UserAgentTokenIdResolver {

    private static final String CACHED_KEY = UserAgentTokenIdResolver.class.getName() + ".AGENT_ID";

    private final HttpCookieResolver httpCookieResolver;

    public DefaultUserAgentTokenIdResolver(String cookieName) {
        httpCookieResolver = new DefaultHttpCookieResolver(cookieName);
    }

    @Override
    public String resolve(HttpServletRequest httpReq) {
        Assert.notNull(httpReq, "require httpReq.");

        String cached = (String) httpReq.getAttribute(CACHED_KEY);
        if (hasText(cached)) {
            return cached;
        }

        String cookieValue = httpCookieResolver.resolve(httpReq);
        if (!hasText(cookieValue)) {
            return null;
        }
        return cached(Base64Utils.decode(cookieValue), httpReq);
    }

    private String cached(String agentId, HttpServletRequest httpReq) {
        httpReq.setAttribute(CACHED_KEY, agentId);
        return agentId;
    }
}
