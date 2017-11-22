/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/08
 */
public class ErrorResponse_toJSONTest {

    @Test
    public void shouldBeErrorNull_whenAllAttributesIsNull() throws JsonProcessingException {
        ErrorResponse input = new ErrorResponse();
        String output = input.toJSON();
        String expected = "{\"error\":null}";

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeOk_whenHasFullAttributes() throws JsonProcessingException {
        ErrorResponse input = new ErrorResponse.Builder()
                .setError("AAA")
                .setErrorDescription("BBB")
                .setErrorUri("CCC")
                .build();
        String output = input.toJSON();
        String expected = "{\"error\":\"AAA\",\"error_description\":\"BBB\",\"error_uri\":\"CCC\"}";

        assertThat(output).isEqualTo(expected);
    }
}
