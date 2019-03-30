/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author jitta
 */
public class OAuth2RefreshTokenResolverTest {

    private static final String TOKEN_NAME = "refresh_token";

    private OAuth2RefreshTokenResolver resolver;

    private HttpServletRequest httpServletRequest;

    @Before
    public void before() {
        resolver = new DefaultOAuth2RefreshTokenResolver();
        httpServletRequest = mock(HttpServletRequest.class);
    }

    @Test
    public void shouldBeRefreshTolken_whenGetTokenName() {
        String output = resolver.getTokenName();
        String expected = TOKEN_NAME;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeAAA_whenRequestParameterIsAAA() {
        String token = "AAA";
        when(httpServletRequest.getParameter(TOKEN_NAME))
                .thenReturn(token);
        String output = resolver.resolve(httpServletRequest);
        String expected = token;
        assertThat(output).isEqualTo(expected);
    }
    
    @Test
    public void shouldBeBBB_whenRequestAttributeIsBBB() {
        String token = "BBB";
        when(httpServletRequest.getAttribute(TOKEN_NAME))
                .thenReturn(token);
        String output = resolver.resolve(httpServletRequest);
        String expected = token;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeCCC_whenRequestCookieIsCCC() {
        String token = "CCC";
        when(httpServletRequest.getCookies())
                .thenReturn(new Cookie[]{
                    new Cookie(TOKEN_NAME, token)
                });
        String output = resolver.resolve(httpServletRequest);
        String expected = token;
        assertThat(output).isEqualTo(expected);
    }
}
