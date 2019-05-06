/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Component
public class DefaultOAuth2AccessTokenResolver implements OAuth2AccessTokenResolver {

    private static final String CACHED_KEY = OAuth2AccessTokenResolver.class.getName() + ".OAUTH2_ACCESS_TOKEN";

    private static final String TOKEN_NAME = "access_token";

    private final List<OAuth2TokenResolver> resovlers = Arrays.asList(
            new RequestHeaderOAuth2TokenResolver(),
            new RequestParameterOAuth2TokenResolver(TOKEN_NAME),
            new RequestAttributeOAuth2TokenResolver(TOKEN_NAME),
            new RequestCookieOAuth2TokenResolver(TOKEN_NAME)
    );

    @Override
    public String resolve(HttpServletRequest httpReq) {
        Assert.notNull(httpReq, "require httpReq.");
        String cached = (String) httpReq.getAttribute(CACHED_KEY);
        if (hasText(cached)) {
            return cached;
        }
        for (OAuth2TokenResolver resolver : resovlers) {
            String token = resolver.resolve(httpReq);
            if (hasText(token)) {
                return cached(token, httpReq);
            }
        }
        return null;
    }

    @Override
    public String getTokenName() {
        return TOKEN_NAME;
    }

    private String cached(String token, HttpServletRequest httpReq) {
        httpReq.setAttribute(CACHED_KEY, token);
        return token;
    }

}
