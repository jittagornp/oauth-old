/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.validator;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/08
 */
public class ValidUriValidatorTest {

    private ValidUri.Validator validator;

    @Before
    public void before() {
        validator = new ValidUri.Validator();
    }

    @Test
    public void shouldBeTrue_whenInputIsNull() {
        String input = null;
        boolean output = validator.isValid(input);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenInputIsEmptyString() {
        String input = "";
        boolean output = validator.isValid(input);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeFalse_whenInvalidInput1() {
        String input = "AAAA";
        boolean output = validator.isValid(input);
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeFalse_whenInvalidInput2() {
        String input = "127.0.0.1";
        boolean output = validator.isValid(input);
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeFalse_whenInvalidInput3() {
        String input = "/callback";
        boolean output = validator.isValid(input);
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenValidInput1() {
        String input = "http://127.0.0.1";
        boolean output = validator.isValid(input);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenValidInput2() {
        String input = "http://localhost:8000";
        boolean output = validator.isValid(input);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenValidInput3() {
        String input = "https://pamarin.com/callback";
        boolean output = validator.isValid(input);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }
}
