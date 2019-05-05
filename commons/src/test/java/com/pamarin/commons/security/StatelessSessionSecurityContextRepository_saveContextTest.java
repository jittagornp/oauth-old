/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 *
 * @author jitta
 */
public class StatelessSessionSecurityContextRepository_saveContextTest {

    private SecurityContextRepository securityContextRepository;

    @Before
    public void before() {
        securityContextRepository = new StatelessSessionSecurityContextRepository();
    }

    @Test
    public void shouldBeDontHaveAnythingOccur_whenHttpRequestIsNull() {
        SecurityContext securityContext = mock(SecurityContext.class);
        HttpServletRequest httpReq = null;
        HttpServletResponse httpResp = null;

        securityContextRepository.saveContext(securityContext, httpReq, httpResp);

        verify(securityContext, never()).getAuthentication();
    }

    @Test
    public void shouldBeDontHaveAnythingOccur_whenHttpSessionIsNull() {
        SecurityContext securityContext = mock(SecurityContext.class);
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpServletResponse httpResp = null;

        securityContextRepository.saveContext(securityContext, httpReq, httpResp);

        verify(securityContext, never()).getAuthentication();
    }

    @Test
    public void shouldBeSave() {
        SecurityContext securityContext = mock(SecurityContext.class);
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(httpReq.getSession(false)).thenReturn(session);
        HttpServletResponse httpResp = null;

        securityContextRepository.saveContext(securityContext, httpReq, httpResp);

        verify(session).setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        verify(securityContext).getAuthentication();
    }
}
