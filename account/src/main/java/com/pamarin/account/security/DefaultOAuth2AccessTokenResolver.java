/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.account.security;

import com.pamarin.commons.resolver.DefaultHttpCookieResolver;
import com.pamarin.commons.resolver.HttpCookieResolver;
import com.pamarin.commons.util.HttpAuthorizeBearerParser;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Component
public class DefaultOAuth2AccessTokenResolver implements OAuth2AccessTokenResolver {

    private static final String ACCESS_TOKEN = "access_token";

    private final HttpAuthorizeBearerParser httpAuthorizeBearerParser;

    private final HttpCookieResolver httpCookieResolver = new DefaultHttpCookieResolver(ACCESS_TOKEN);

    @Autowired
    public DefaultOAuth2AccessTokenResolver(HttpAuthorizeBearerParser httpAuthorizeBearerParser) {
        this.httpAuthorizeBearerParser = httpAuthorizeBearerParser;
    }

    @Override
    public String resolve(HttpServletRequest httpReq) {
        String authorization = httpReq.getHeader("Authorization");
        if (hasText(authorization)) {
            String token = httpAuthorizeBearerParser.parse(authorization);
            if (hasText(token)) {
                return token;
            }
        }

        String token = httpReq.getParameter(ACCESS_TOKEN);
        if (hasText(token)) {
            return token;
        }

        String tokenAttr = (String) httpReq.getAttribute(ACCESS_TOKEN);
        if (hasText(tokenAttr)) {
            return tokenAttr;
        }

        return httpCookieResolver.resolve(httpReq);
    }

    @Override
    public String getTokenName() {
        return ACCESS_TOKEN;
    }

}
