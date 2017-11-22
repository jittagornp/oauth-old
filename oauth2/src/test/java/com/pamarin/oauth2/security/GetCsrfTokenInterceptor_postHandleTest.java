/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/19
 */
public class GetCsrfTokenInterceptor_postHandleTest {

    private GetCsrfTokenIntorceptor interceptor;

    private HttpServletRequest httpReq;
    private HttpServletResponse httpResp;

    @Before
    public void before() {
        interceptor = new GetCsrfTokenIntorceptor();
        httpReq = mock(HttpServletRequest.class);
        httpResp = mock(HttpServletResponse.class);
        ReflectionTestUtils.setField(
                interceptor,
                "hostUrl",
                "http://localhost"
        );
    }

    @Test
    public void shouldBeNotHappened() throws Exception {
        interceptor.postHandle(httpReq, httpResp, null, null);
    }

    @Test
    public void shouldBeOk() throws Exception {
        ModelAndView modelAndView = mock(ModelAndView.class);
        when(httpReq.getAttribute("csrf-param"))
                .thenReturn("X-CSRF-Token");
        when(httpReq.getAttribute("csrf-token"))
                .thenReturn("xyz");
        interceptor.postHandle(httpReq, httpResp, null, modelAndView);
        verify(modelAndView).addObject("csrfParam", "X-CSRF-Token");
        verify(httpResp).setHeader("X-CSRF-Token", "xyz");
    }

}
