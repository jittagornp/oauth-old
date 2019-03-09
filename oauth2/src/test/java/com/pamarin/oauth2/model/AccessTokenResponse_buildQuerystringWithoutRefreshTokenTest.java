/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/26
 */
public class AccessTokenResponse_buildQuerystringWithoutRefreshTokenTest {

    @Test
    public void shouldBeOk() {

        AccessTokenResponse input = AccessTokenResponse.builder()
                .accessToken("1234")
                .expiresIn(3600)
                .refreshToken("5678")
                .state("XYZ")
                .tokenType("bearer")
                .build();

        String output = input.buildQuerystringWithoutRefreshToken();
        String expected = "access_token=1234&state=XYZ&token_type=bearer&expires_in=3600";

        assertThat(output).isEqualTo(expected);
    }

}
