/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/21
 */
public class LoginSession_wasCreatedTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private LoginSession loginSession;

    @Before
    public void before() {
        loginSession = new DefaultLoginSession();
    }

    @Test
    public void shouldBeFalse_whenNotLogin() {
        boolean output = loginSession.wasCreated();
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenLogedIn() {
        loginSession.create(DefaultUserSession.builder()
                .id("00000000000000000000000000000000")
                .username("test")
                .password(null)
                .build());

        boolean output = loginSession.wasCreated();
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

}
