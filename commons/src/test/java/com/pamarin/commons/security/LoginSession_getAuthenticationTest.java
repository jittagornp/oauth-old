/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.exception.AuthenticationException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author jitta
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class LoginSession_getAuthenticationTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private LoginSession loginSession;

    @Before
    public void before() {
        loginSession = new DefaultLoginSession();
    }

    @Test
    public void shouldBeThrowAuthenticationException_whenSecurityContextIsNull() {
        exception.expect(AuthenticationException.class);
        exception.expectMessage("Please login, authentication is null.");

        SecurityContextHolder.clearContext();
        loginSession.getAuthentication();
    }

    @Test
    public void shouldBeThrowAuthenticationException_whenAuthenticationIsNull() {
        exception.expect(AuthenticationException.class);
        exception.expectMessage("Please login, authentication is null.");

        SecurityContext context = new SecurityContextImpl();
        SecurityContextHolder.setContext(context);
        loginSession.getAuthentication();
    }

    @Test
    public void shouldBeOk() {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(null, null);
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(context);

        Authentication output = loginSession.getAuthentication();
        Authentication expected = authenticationToken;
        assertThat(output).isEqualTo(expected);
    }

}
