/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 *
 * @author jitta
 */
public class StatelessSessionSecurityContextRepository_containsContextTest {

    private SecurityContextRepository securityContextRepository;

    @Before
    public void before() {
        securityContextRepository = new StatelessSessionSecurityContextRepository();
    }

    @Test
    public void shouldBeFalse_whenHttpRequestIsNull() {
        HttpServletRequest httpReq = null;
        boolean output = securityContextRepository.containsContext(httpReq);
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeFalse_whenHttpSessionIsNull() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        boolean output = securityContextRepository.containsContext(httpReq);
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeFalse_whenHttpSessionAttributeIsNull() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(httpReq.getSession(false)).thenReturn(session);
        boolean output = securityContextRepository.containsContext(httpReq);
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeFalse_whenHttpSessionAttributeIsNotSecurityContext() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(httpReq.getSession(false)).thenReturn(session);
        when(session.getAttribute("SPRING_SECURITY_CONTEXT")).thenReturn("test");

        boolean output = securityContextRepository.containsContext(httpReq);
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(httpReq.getSession(false)).thenReturn(session);
        when(session.getAttribute("SPRING_SECURITY_CONTEXT")).thenReturn(new SecurityContextImpl());

        boolean output = securityContextRepository.containsContext(httpReq);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }
}
