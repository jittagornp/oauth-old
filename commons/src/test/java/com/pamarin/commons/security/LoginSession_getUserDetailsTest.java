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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/21
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class LoginSession_getUserDetailsTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private LoginSession loginSession;

    @Before
    public void before() {
        loginSession = new DefaultLoginSession();
    }
    
    @Test
    public void shouldBeThrowAuthenticationException_whenPrincipalIsNull() {

        exception.expect(AuthenticationException.class);
        exception.expectMessage("Please login, principal is null.");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(null, null);
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(context);

        loginSession.getUserDetails();
    }

    @Test
    public void shouldBeThrowAuthenticationException_whenAuthenticationIsNull() {

        exception.expect(AuthenticationException.class);
        exception.expectMessage("Please login, principal is not user details.");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("anonymousUser", null);
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(context);

        loginSession.getUserDetails();
    }

    @Test
    public void shouldBeOk() {
        UserDetails input = UserDetailsStub.get();

        loginSession.create(input);
        UserDetails output = loginSession.getUserDetails();
        UserDetails expected = input;

        assertThat(output).isEqualTo(expected);
    }
}
