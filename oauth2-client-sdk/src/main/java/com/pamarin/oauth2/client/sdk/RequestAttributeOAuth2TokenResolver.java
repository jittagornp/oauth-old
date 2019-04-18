/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jitta
 */
public class RequestAttributeOAuth2TokenResolver implements OAuth2TokenResolver {

    private final String tokenName;

    public RequestAttributeOAuth2TokenResolver(String tokenName) {
        this.tokenName = tokenName;
    }

    @Override
    public String resolve(HttpServletRequest httpReq) {
        return (String) httpReq.getAttribute(tokenName);
    }
}
