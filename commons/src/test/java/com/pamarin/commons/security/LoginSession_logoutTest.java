/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.provider.HttpSessionProvider;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author jitta
 */
public class LoginSession_logoutTest {

    private LoginSession loginSession;

    private HttpSessionProvider httpSessionProvider;

    private HttpSession session;

    @Before
    public void before() {
        session = mock(HttpSession.class);
        httpSessionProvider = mock(HttpSessionProvider.class);
        loginSession = new DefaultLoginSession();
        ReflectionTestUtils.setField(
                loginSession,
                "httpSessionProvider",
                httpSessionProvider
        );
    }

    @Test
    public void shouldBeCallInvalidate_whenSessionIsNotNull() {
        when(httpSessionProvider.provide()).thenReturn(session);

        loginSession.logout();

        verify(session, times(1)).invalidate();
        verify(session).setMaxInactiveInterval(0);
    }
}
