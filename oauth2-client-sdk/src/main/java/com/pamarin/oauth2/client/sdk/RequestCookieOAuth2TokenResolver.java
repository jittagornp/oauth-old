/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.resolver.DefaultHttpCookieResolver;
import com.pamarin.commons.resolver.HttpCookieResolver;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jitta
 */
public class RequestCookieOAuth2TokenResolver implements OAuth2TokenResolver {

    private final HttpCookieResolver httpCookieResolver;

    public RequestCookieOAuth2TokenResolver(String tokenName) {
        this.httpCookieResolver = new DefaultHttpCookieResolver(tokenName);
    }

    @Override
    public String resolve(HttpServletRequest httpReq) {
        return httpCookieResolver.resolve(httpReq);
    }
}
