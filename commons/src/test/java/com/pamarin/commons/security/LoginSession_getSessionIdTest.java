/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.provider.HttpSessionProvider;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author jitta
 */
public class LoginSession_getSessionIdTest {

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
    public void shouldBeNull_whenSessionIsNull() {
        when(httpSessionProvider.provide()).thenReturn(null);

        String output = loginSession.getSessionId();
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeOk() {
        String sessionId = UUID.randomUUID().toString();
        when(session.getId()).thenReturn(sessionId);
        when(httpSessionProvider.provide()).thenReturn(session);

        String output = loginSession.getSessionId();
        String expected = sessionId;
        assertThat(output).isEqualTo(expected);
    }
}
