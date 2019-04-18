/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import javax.servlet.http.HttpServletRequest;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
public class RequestParameterOAuth2TokenResolver implements OAuth2TokenResolver {

    private final String tokenName;

    public RequestParameterOAuth2TokenResolver(String tokenName) {
        this.tokenName = tokenName;
    }

    @Override
    public String resolve(HttpServletRequest httpReq) {
        if ("POST".equalsIgnoreCase(httpReq.getMethod())) {
            if (isQuerystringParameter(httpReq)) {
                return null;
            }
            return httpReq.getParameter(tokenName);
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

    @Override
    public String getTokenName() {
        return tokenName;
    }

}
