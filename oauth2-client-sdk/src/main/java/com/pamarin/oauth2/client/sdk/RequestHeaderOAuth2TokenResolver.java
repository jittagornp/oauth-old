/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.exception.InvalidHttpAuthorizationException;
import com.pamarin.commons.util.DefaultHttpAuthorizationParser;
import com.pamarin.commons.util.DefaultHttpAuthorizeBearerParser;
import com.pamarin.commons.util.HttpAuthorizeBearerParser;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jitta
 */
public class RequestHeaderOAuth2TokenResolver implements OAuth2TokenResolver {

    private final String tokenName;

    private final HttpAuthorizeBearerParser httpAuthorizeBearerParser;

    public RequestHeaderOAuth2TokenResolver(String tokenName) {
        this.tokenName = tokenName;
        this.httpAuthorizeBearerParser = new DefaultHttpAuthorizeBearerParser(new DefaultHttpAuthorizationParser());
    }

    @Override
    public String resolve(HttpServletRequest httpReq) {
        try {
            String authorization = httpReq.getHeader("Authorization");
            return httpAuthorizeBearerParser.parse(authorization);
        } catch (InvalidHttpAuthorizationException ex) {
            return null;
        }
    }

    @Override
    public String getTokenName() {
        return tokenName;
    }

}
