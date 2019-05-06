/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import javax.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
public class RequestParameterOAuth2TokenResolver implements OAuth2TokenResolver {

    private static final String POST_METHOD = "POST";

    private final String tokenName;

    private final String cachedKey;

    public RequestParameterOAuth2TokenResolver(String tokenName) {
        this.tokenName = tokenName;
        this.cachedKey = OAuth2RefreshTokenResolver.class.getName() + "." + tokenName;
    }

    @Override
    public String resolve(HttpServletRequest httpReq) {
        Assert.notNull(httpReq, "require httpReq.");
        String cached = (String) httpReq.getAttribute(cachedKey);
        if (hasText(cached)) {
            return cached;
        }
        if (POST_METHOD.equalsIgnoreCase(httpReq.getMethod())) {
            if (isQuerystringParameter(httpReq)) {
                return null;
            }
            return cached(httpReq.getParameter(tokenName), httpReq);
        }
        return null;
    }

    private boolean isQuerystringParameter(HttpServletRequest httpReq) {
        String queryString = httpReq.getQueryString();
        if (!hasText(queryString)) {
            return false;
        }
        return queryString.contains(tokenName);
    }

    private String cached(String token, HttpServletRequest httpReq) {
        httpReq.setAttribute(cachedKey, token);
        return token;
    }
}
