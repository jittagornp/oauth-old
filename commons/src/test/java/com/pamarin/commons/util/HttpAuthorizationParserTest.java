/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

import com.pamarin.commons.exception.InvalidHttpAuthorizationException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
public class HttpAuthorizationParserTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private HttpAuthorizationParser parser;

    @Before
    public void withImplementation() {
        parser = new DefaultHttpAuthorizationParser();
    }

    @Test
    public void shouldBeRequiredTypeAndAuthorization_whenAllInputsIsNull() {

        exception.expect(InvalidHttpAuthorizationException.class);
        exception.expectMessage("Required type and authorization.");

        String type = null;
        String authorization = null;
        String output = parser.parse(type, authorization);

    }

    @Test
    public void shouldBeRequiredAuthorization_whenAuthorizationIsNull() {

        exception.expect(InvalidHttpAuthorizationException.class);
        exception.expectMessage("Required type and authorization.");

        String type = "basic";
        String authorization = null;
        String output = parser.parse(type, authorization);

    }

    @Test
    public void shouldBeRequiredType_whenTypeIsNull() {

        exception.expect(InvalidHttpAuthorizationException.class);
        exception.expectMessage("Required type and authorization.");

        String type = null;
        String authorization = "xyz";
        String output = parser.parse(type, authorization);

    }
    
    @Test
    public void shouldBeInvalidCredentialValue_whenTypeIsNull() {

        exception.expect(InvalidHttpAuthorizationException.class);
        exception.expectMessage("Invalid Credential value.");

        String type = "basic";
        String authorization = "xyz";
        String output = parser.parse(type, authorization);

    }

    @Test
    public void shouldBeOk() {
        String type = "basic";
        String authorization = "basic xyz";
        String output = parser.parse(type, authorization);
        String expected = "xyz";
        assertThat(output).isEqualTo(expected);
    }

}
