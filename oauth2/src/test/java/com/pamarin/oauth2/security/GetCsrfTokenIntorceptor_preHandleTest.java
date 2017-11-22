/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.security;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.method.HandlerMethod;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/16
 */
public class GetCsrfTokenIntorceptor_preHandleTest {

    private GetCsrfTokenIntorceptor interceptor;

    private HttpServletRequest request;
    private HttpServletResponse response;

    private AuthenticityToken authenticityToken;

    @Before
    public void before() {
        interceptor = new GetCsrfTokenIntorceptor();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        authenticityToken = mock(AuthenticityToken.class);
        when(authenticityToken.random())
                .thenReturn(new AuthenticityToken.RandomOutput("1234", "xyz"));
        when(request.getSession())
                .thenReturn(mock(HttpSession.class));

        ReflectionTestUtils.setField(
                interceptor,
                "authenticityToken",
                authenticityToken
        );
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
    public void shouldBeTrue() throws Exception {
        boolean output = interceptor.preHandle(null, null, null);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNeverCallSetAttribute_whenDontHaveAnnotationGetCsrfToken() throws Exception {
        boolean output = interceptor.preHandle(request, response, getHandlerMethod("normal"));
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
        verify(request, never()).setAttribute(any(String.class), any(String.class));
    }

    @Test
    public void shouldBeCallSetAttribute_whenHasAnnotationGetCsrfToken() throws Exception {
        boolean output = interceptor.preHandle(request, response, getHandlerMethod("login"));
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
        verify(request).setAttribute("csrf-param", "X-CSRF-Token");
    }
}
