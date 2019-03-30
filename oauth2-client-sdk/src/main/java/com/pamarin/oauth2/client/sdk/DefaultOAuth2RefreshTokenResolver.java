/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.resolver.DefaultHttpCookieResolver;
import com.pamarin.commons.resolver.HttpCookieResolver;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Component
public class DefaultOAuth2RefreshTokenResolver implements OAuth2RefreshTokenResolver {

    private static final String REFRESH_TOKEN = "refresh_token";

    private final HttpCookieResolver httpCookieResolver = new DefaultHttpCookieResolver(REFRESH_TOKEN);

    @Override
    public String resolve(HttpServletRequest httpReq) {
        String token = httpReq.getParameter(REFRESH_TOKEN);
        if (hasText(token)) {
            return token;
        }

        String tokenAttr = (String) httpReq.getAttribute(REFRESH_TOKEN);
        if (hasText(tokenAttr)) {
            return tokenAttr;
        }

        return httpCookieResolver.resolve(httpReq);
    }

    @Override
    public String getTokenName() {
        return REFRESH_TOKEN;
    }

}
