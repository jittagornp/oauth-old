/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/23
 */
public class CsrfInterceptor_postHandleTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private CsrfInterceptor interceptor;

    private HttpServletRequest httpReq;
    private HttpServletResponse httpResp;

    private AuthenticityToken authenticityToken;

    @Before
    public void before() {
        interceptor = new CsrfInterceptor("https://pamarin.com", 44);
        httpReq = mock(HttpServletRequest.class);
        httpResp = mock(HttpServletResponse.class);
        authenticityToken = mock(AuthenticityToken.class);

        HttpSession httpSession = mock(HttpSession.class);
        when(httpReq.getSession()).thenReturn(httpSession);

        interceptor.setAuthenticityToken(authenticityToken);
    }

    private HandlerMethod getHandlerMethod(String methodName) throws NoSuchMethodException {
        HandlerMethod handlerMethod = mock(HandlerMethod.class);
        Method method = getClass().getDeclaredMethod(methodName);
        when(handlerMethod.getMethod()).thenReturn(method);
        return handlerMethod;
    }

    public void normal() {

    }

    @GetCsrfToken
    public void login() {

    }

    @Test
    public void shouldBeNotHappend_whenModelAndViewAndHandlerObjectIsNull() throws Exception {
        interceptor.postHandle(httpReq, httpResp, null, null);
    }

    @Test
    public void shouldBeNotHappend_whenObjectItsNotHandlerMethod() throws Exception {
        interceptor.postHandle(httpReq, httpResp, new Object(), new ModelAndView());
    }

    @Test
    public void shouldBeNotHappend_whenMethodDontHaveGetCsrfToken() throws Exception {
        interceptor.postHandle(httpReq, httpResp, getHandlerMethod("normal"), new ModelAndView());
    }

    @Test
    public void shouldBeOk() throws Exception {
        when(authenticityToken.random()).thenReturn(new AuthenticityToken.RandomOutput("abc", "xyz"));
        when(httpReq.getServletPath()).thenReturn("/login");
        when(httpResp.getStatus()).thenReturn(HttpServletResponse.SC_OK);

        ModelAndView modelAndView = mock(ModelAndView.class);

        interceptor.postHandle(httpReq, httpResp, getHandlerMethod("login"), modelAndView);

        verify(modelAndView).addObject("csrfParam", "X-CSRF-Token");
        verify(modelAndView).addObject("csrfToken", "xyz");
        verify(httpReq.getSession()).setAttribute("X-CSRF-Token:/login", "abc");
        verify(httpResp).setHeader("X-CSRF-Token", "xyz");
    }
}
