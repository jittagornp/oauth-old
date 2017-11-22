/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.security;

import com.pamarin.commons.security.AuthenticityToken;
import com.pamarin.oauth2.exception.InvalidCsrfTokenException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/18
 */
public class CsrfVerificationInterceptorTest {

    private static final String CSRF_PARAM = "X-CSRF-Token";
    private static final String CSRF_COOKIE = "csrf-token";

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private CsrfInterceptor interceptor;

    private HttpServletRequest httpReq;
    private HttpServletResponse httpResp;

    private AuthenticityToken authenticityToken;

    @Before
    public void before() {
        interceptor = new CsrfInterceptor();
        httpReq = mock(HttpServletRequest.class);
        httpResp = mock(HttpServletResponse.class);
        authenticityToken = mock(AuthenticityToken.class);

        HttpSession httpSession = mock(HttpSession.class);
        when(httpReq.getSession()).thenReturn(httpSession);

        ReflectionTestUtils.setField(
                interceptor,
                "authenticityToken",
                authenticityToken
        );
    }

    @Test
    public void shouldBeTrue_whenHttpMethodIsGET() throws Exception {
        when(httpReq.getMethod()).thenReturn("GET");
        boolean output = interceptor.preHandle(httpReq, httpResp, null);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeErrorDoubleSubmitCookie_whenDontHaveParameterToken() throws Exception {
        when(httpReq.getMethod()).thenReturn("POST");
        when(httpReq.getParameter(CSRF_PARAM)).thenReturn(null);
        when(httpReq.getHeader(CSRF_PARAM)).thenReturn(null);
        when(httpReq.getCookies()).thenReturn(null);

        exception.expect(InvalidCsrfTokenException.class);
        exception.expectMessage("Invalid Double Submit Cookie, don't have token");

        boolean output = interceptor.preHandle(httpReq, httpResp, null);

    }

    @Test
    public void shouldBeErrorDoubleSubmitCookie_whenDontHaveCookieToken() throws Exception {
        when(httpReq.getMethod()).thenReturn("POST");
        when(httpReq.getParameter(CSRF_PARAM)).thenReturn("xyz");
        when(httpReq.getHeader(CSRF_PARAM)).thenReturn(null);
        when(httpReq.getCookies()).thenReturn(null);

        exception.expect(InvalidCsrfTokenException.class);
        exception.expectMessage("Invalid Double Submit Cookie, don't have token");

        boolean output = interceptor.preHandle(httpReq, httpResp, null);

    }

    @Test
    public void shouldBeErrorDoubleSubmitCookie_whenInvalidTokenParameter() throws Exception {
        when(httpReq.getMethod()).thenReturn("POST");
        when(httpReq.getParameter(CSRF_PARAM)).thenReturn("xyz");
        when(httpReq.getHeader(CSRF_PARAM)).thenReturn(null);
        when(httpReq.getCookies()).thenReturn(new Cookie[]{
            new Cookie(CSRF_COOKIE, "abc")
        });

        exception.expect(InvalidCsrfTokenException.class);
        exception.expectMessage("Invalid Double Submit Cookie");

        boolean output = interceptor.preHandle(httpReq, httpResp, null);

    }

    @Test
    public void shouldBeErrorDoubleSubmitCookie_whenInvalidTokenHeader() throws Exception {
        when(httpReq.getMethod()).thenReturn("POST");
        when(httpReq.getParameter(CSRF_PARAM)).thenReturn(null);
        when(httpReq.getHeader(CSRF_PARAM)).thenReturn("xyz");
        when(httpReq.getCookies()).thenReturn(new Cookie[]{
            new Cookie(CSRF_COOKIE, "abc")
        });

        exception.expect(InvalidCsrfTokenException.class);
        exception.expectMessage("Invalid Double Submit Cookie");

        boolean output = interceptor.preHandle(httpReq, httpResp, null);

    }

    @Test
    public void shouldBeErrorDoubleSubmitCookie_whenCantDecodeAuthenticity() throws Exception {
        when(httpReq.getMethod()).thenReturn("POST");
        when(httpReq.getParameter(CSRF_PARAM)).thenReturn(null);
        when(httpReq.getHeader(CSRF_PARAM)).thenReturn("xyz");
        when(httpReq.getCookies()).thenReturn(new Cookie[]{
            new Cookie(CSRF_COOKIE, "xyz")
        });
        when(authenticityToken.decode("xyz")).thenReturn(null);

        exception.expect(InvalidCsrfTokenException.class);
        exception.expectMessage("Can't decode csrf token");

        boolean output = interceptor.preHandle(httpReq, httpResp, null);

    }

    @Test
    public void shouldBeErrorDoubleSubmitCookie_whenNotFoundTokenInUserSession() throws Exception {
        when(httpReq.getMethod()).thenReturn("POST");
        when(httpReq.getParameter(CSRF_PARAM)).thenReturn(null);
        when(httpReq.getHeader(CSRF_PARAM)).thenReturn("xyz");
        when(httpReq.getCookies()).thenReturn(new Cookie[]{
            new Cookie(CSRF_COOKIE, "xyz")
        });
        when(authenticityToken.decode("xyz")).thenReturn("1234");
        when(httpReq.getSession().getAttribute(CSRF_PARAM + ":1234")).thenReturn(null);

        exception.expect(InvalidCsrfTokenException.class);
        exception.expectMessage("Not found csrf token in user session");

        boolean output = interceptor.preHandle(httpReq, httpResp, null);

    }

    @Test
    public void shouldBeOk_whenValidToken() throws Exception {
        when(httpReq.getMethod()).thenReturn("POST");
        when(httpReq.getParameter(CSRF_PARAM)).thenReturn(null);
        when(httpReq.getHeader(CSRF_PARAM)).thenReturn("xyz");
        when(httpReq.getCookies()).thenReturn(new Cookie[]{
            new Cookie(CSRF_COOKIE, "xyz")
        });
        when(authenticityToken.decode("xyz")).thenReturn("1234");
        when(httpReq.getSession().getAttribute(CSRF_PARAM + ":1234")).thenReturn(true);

        boolean output = interceptor.preHandle(httpReq, httpResp, null);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }
}
