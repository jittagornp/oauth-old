/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/08
 */
public class ErrorResponse_buildQuerystringTest {

    @Test
    public void shouldBeHasOnlyErrorParameter() {
        ErrorResponse input = ErrorResponse.invalidRequest();
        String output = input.buildQuerystring();
        String expected = "error=invalid_request";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeHaveErrorAndErrorDescriptionParameters() {
        ErrorResponse input = new ErrorResponse.Builder()
                .setError("AAA")
                .setErrorDescription("BBB")
                .build();
        String output = input.buildQuerystring();
        String expected = "error=AAA&error_description=BBB";
        assertThat(output).isEqualTo(expected);
    }

     @Test
    public void shouldBeHaveAllParameters() {
        ErrorResponse input = new ErrorResponse.Builder()
                .setError("AAA")
                .setErrorDescription("BBB")
                .setErrorUri("CCC")
                .build();
        String output = input.buildQuerystring();
        String expected = "error=AAA&error_description=BBB&error_uri=CCC";
        assertThat(output).isEqualTo(expected);
    }
}
