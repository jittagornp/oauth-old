/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/25
 */
public class AuthorizationRequest_responseTypeIsXXXTest {

    @Test
    public void shouldBeFalse_whenResponseTypeIsAAA() {
        AuthorizationRequest input = AuthorizationRequest.builder()
                .responseType("AAA")
                .build();
        boolean output = input.responseTypeIsCode();
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenResponseTypeIsCode() {
        AuthorizationRequest input = AuthorizationRequest.builder()
                .responseType("code")
                .build();
        boolean output = input.responseTypeIsCode();
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }
}
