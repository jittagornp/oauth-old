/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Component
public class DefaultOAuth2AccessTokenResolver implements OAuth2AccessTokenResolver {

    private static final String TOKEN_NAME = "access_token";

    private final List<OAuth2TokenResolver> resovlers = Arrays.asList(
            new RequestHeaderOAuth2TokenResolver(),
            new RequestParameterOAuth2TokenResolver(TOKEN_NAME),
            new RequestAttributeOAuth2TokenResolver(TOKEN_NAME),
            new RequestCookieOAuth2TokenResolver(TOKEN_NAME)
    );

    @Override
    public String resolve(HttpServletRequest httpReq) {
        for (OAuth2TokenResolver resolver : resovlers) {
            String token = resolver.resolve(httpReq);
            if (hasText(token)) {
                return token;
            }
        }
        return null;
    }

    @Override
    public String getTokenName() {
        return TOKEN_NAME;
    }

}
