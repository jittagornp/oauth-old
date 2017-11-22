/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.exception.AuthenticationException;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.commons.security.DefaultUserSession;
import com.pamarin.commons.security.UserSession;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/21
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class LoginSession_getUserSessionTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private LoginSession loginSession;

    @Before
    public void before() {
        loginSession = new DefaultLoginSession();
    }

    @Test
    public void shouldBeThrowAuthenticationException_whenAuthenticationIsNull() {

        exception.expect(AuthenticationException.class);
        exception.expectMessage("Please login.");

        loginSession.getUserSession();
    }

    @Test
    @WithMockUser(username = "test")
    public void shouldBeThrowAuthenticationException_whenAuthenticationItsNotUserLogin() {

        exception.expect(AuthenticationException.class);
        exception.expectMessage("Please login, it's not user session.");

        loginSession.getUserSession();
    }

    @Test
    public void shouldBeOk() {
        UserSession input = DefaultUserSession.builder()
                .id(1L)
                .username("test")
                .password(null)
                .build();

        loginSession.create(input);
        UserSession output = loginSession.getUserSession();
        UserSession expected = input;

        assertThat(output).isEqualTo(expected);
    }
}
