/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.security.core.context.SecurityContext;
import static org.springframework.security.core.context.SecurityContextHolder.createEmptyContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 *
 * @author jitta
 */
public class StatelessSessionSecurityContextRepository_loadContextTest {

    private SecurityContextRepository securityContextRepository;

    @Before
    public void before() {
        securityContextRepository = new StatelessSessionSecurityContextRepository();
    }

    @Test
    public void shouldBeEmptyContext_whenInputIsNull() {
        HttpRequestResponseHolder input = null;
        SecurityContext output = securityContextRepository.loadContext(input);
        SecurityContext expected = createEmptyContext();
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeEmptyContext_whenHttpRequestIsNull() {
        HttpServletRequest httpReq = null;
        HttpServletResponse httpResp = null;
        HttpRequestResponseHolder input = new HttpRequestResponseHolder(httpReq, httpResp);
        SecurityContext output = securityContextRepository.loadContext(input);
        SecurityContext expected = createEmptyContext();
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeEmptyContext_whenHttpSessionIsNull() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpServletResponse httpResp = null;
        HttpRequestResponseHolder input = new HttpRequestResponseHolder(httpReq, httpResp);
        SecurityContext output = securityContextRepository.loadContext(input);
        SecurityContext expected = createEmptyContext();
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeEmptyContext_whenHttpSessionAttributeIsNull() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(httpReq.getSession(false)).thenReturn(session);

        HttpServletResponse httpResp = null;
        HttpRequestResponseHolder input = new HttpRequestResponseHolder(httpReq, httpResp);
        SecurityContext output = securityContextRepository.loadContext(input);
        SecurityContext expected = createEmptyContext();
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeEmptyContext_whenHttpSessionAttributeIsNotSecurityContext() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(httpReq.getSession(false)).thenReturn(session);
        when(session.getAttribute("SPRING_SECURITY_CONTEXT")).thenReturn("test");

        HttpServletResponse httpResp = null;
        HttpRequestResponseHolder input = new HttpRequestResponseHolder(httpReq, httpResp);
        SecurityContext output = securityContextRepository.loadContext(input);
        SecurityContext expected = createEmptyContext();
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNotEmptyContext_whenHttpSessionAttributeIsSecurityContext() {
        SecurityContext context = new SecurityContextImpl();
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(httpReq.getSession(false)).thenReturn(session);
        when(session.getAttribute("SPRING_SECURITY_CONTEXT")).thenReturn(context);

        HttpServletResponse httpResp = null;
        HttpRequestResponseHolder input = new HttpRequestResponseHolder(httpReq, httpResp);
        SecurityContext output = securityContextRepository.loadContext(input);
        SecurityContext expected = context;
        assertThat(output).isEqualTo(expected);
    }
}
