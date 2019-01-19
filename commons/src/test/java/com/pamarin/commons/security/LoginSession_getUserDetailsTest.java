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
    public void shouldBeThrowAuthenticationException_whenAuthenticationIsNull() {

        exception.expect(AuthenticationException.class);
        exception.expectMessage("Please login.");

        loginSession.getUserDetails();
    }
//TODO
//
//    @Test
//    public void shouldBeOk() {
//        UserDetails input = UserDetailsStub.get();
//
//        loginSession.create(input);
//        UserDetails output = loginSession.getUserDetails();
//        UserDetails expected = input;
//
//        assertThat(output).isEqualTo(expected);
//    }
}
