/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.validator;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/09
 */
public class GrantTypeValidatorTest {

    private GrantType.Validator validator;

    @Before
    public void before() {
        validator = new GrantType.Validator();
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
    public void shouldBeTrue_whenInputIsAuthorizationCode() {
        String input = "authorization_code";
        boolean output = validator.isValid(input);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenInputIsRefreshToken() {
        String input = "refresh_token";
        boolean output = validator.isValid(input);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }
    
    @Test
    public void shouldBeFalse_whenInputIsAAA() {
        String input = "AAA";
        boolean output = validator.isValid(input);
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }
}
