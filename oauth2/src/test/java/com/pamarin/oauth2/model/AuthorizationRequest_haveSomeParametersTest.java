/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/11
 */
public class AuthorizationRequest_haveSomeParametersTest {

    @Test
    public void shouldBeFalse_whenNullAllParameters() {
        AuthorizationRequest input = AuthorizationRequest.builder().build();
        boolean output = input.haveSomeParameters();
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenHaveSomeParameters1() {
        AuthorizationRequest input = AuthorizationRequest.builder()
                .clientId("123456")
                .build();
        boolean output = input.haveSomeParameters();
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenHaveSomeParameters2() {
        AuthorizationRequest input = AuthorizationRequest.builder()
                .redirectUri("http://localhost")
                .build();
        boolean output = input.haveSomeParameters();
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenHaveSomeParameters3() {
        AuthorizationRequest input = AuthorizationRequest.builder()
                .responseType("code")
                .build();
        boolean output = input.haveSomeParameters();
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenHaveSomeParameters4() {
        AuthorizationRequest input = AuthorizationRequest.builder()
                .scope("read")
                .build();
        boolean output = input.haveSomeParameters();
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenHaveSomeParameters5() {
        AuthorizationRequest input = AuthorizationRequest.builder()
                .state("XYZ")
                .build();
        boolean output = input.haveSomeParameters();
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }
}
