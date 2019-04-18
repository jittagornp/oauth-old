/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.util.HttpAuthorizeBearerParser;
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
public class OAuth2AccessTokenResolverTest {

    private static final String TOKEN_NAME = "access_token";

    private OAuth2AccessTokenResolver resolver;

    private HttpServletRequest httpServletRequest;

    private HttpAuthorizeBearerParser httpAuthorizeBearerParser;

    @Before
    public void before() {
        httpAuthorizeBearerParser = mock(HttpAuthorizeBearerParser.class);
        resolver = new DefaultOAuth2AccessTokenResolver();
        httpServletRequest = mock(HttpServletRequest.class);
    }

    @Test
    public void shouldBeAccessToken_whenGetTokenName() {
        String output = resolver.getTokenName();
        String expected = TOKEN_NAME;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenRequestHeaderAuthorizationBasicIsXXX() {
        String token = "XXX";
        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Basic " + token);
        when(httpAuthorizeBearerParser.parse("Basic " + token))
                .thenReturn(null);

        String output = resolver.resolve(httpServletRequest);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeXXX_whenRequestHeaderAuthorizationBearerIsXXX() {
        String token = "XXX";
        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        when(httpAuthorizeBearerParser.parse("Bearer " + token))
                .thenReturn(token);

        String output = resolver.resolve(httpServletRequest);
        String expected = token;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenRequestParameterIsAAA() {
        String token = "AAA";
        when(httpServletRequest.getParameter(TOKEN_NAME))
                .thenReturn(token);
        String output = resolver.resolve(httpServletRequest);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenRequestMethodIsPostAndParameterIsAAAButIsQuerystring() {
        String token = "AAA";
        when(httpServletRequest.getParameter(TOKEN_NAME))
                .thenReturn(token);
         when(httpServletRequest.getQueryString())
                .thenReturn("access_token=xxxx&state=yyyy");
        when(httpServletRequest.getMethod())
                .thenReturn("POST");
        String output = resolver.resolve(httpServletRequest);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeAAA_whenRequestMethodIsPostAndParameterIsAAA() {
        String token = "AAA";
        when(httpServletRequest.getParameter(TOKEN_NAME))
                .thenReturn(token);
        when(httpServletRequest.getMethod())
                .thenReturn("POST");
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
