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
public class DefaultOAuth2RefreshTokenResolver implements OAuth2RefreshTokenResolver {

    private static final String ACCESS_TOKEN = "refresh_token";

    private final List<OAuth2TokenResolver> resovlers = Arrays.asList(
            new RequestParameterOAuth2TokenResolver(ACCESS_TOKEN),
            new RequestAttributeOAuth2TokenResolver(ACCESS_TOKEN),
            new RequestCookieOAuth2TokenResolver(ACCESS_TOKEN)
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
        return ACCESS_TOKEN;
    }

}
