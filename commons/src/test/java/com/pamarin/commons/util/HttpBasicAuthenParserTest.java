/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

import com.pamarin.commons.exception.InvalidHttpBasicAuthenException;
import com.pamarin.commons.util.HttpBasicAuthenParser.Output;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/26
 */
public class HttpBasicAuthenParserTest {

    private HttpBasicAuthenParser parser;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void before() {
        parser = new DefaultHttpBasicAuthenParser();

        ReflectionTestUtils.setField(
                parser,
                "parser",
                new DefaultHttpAuthorizationParser()
        );
    }

    @Test
    public void shouldBeInvalidCredentialValue_whenInputIsBasicEmptyString() {

        exception.expect(InvalidHttpBasicAuthenException.class);
        exception.expectMessage("Invalid Credential value (Empty value).");

        String input = "Basic ";
        Output output = parser.parse(input);

    }

    @Test
    public void shouldBeInvalidCredentialValue_whenInputIsBasicDGVzdA() {

        exception.expect(InvalidHttpBasicAuthenException.class);
        exception.expectMessage("Invalid Credential value (Can't decode base64).");

        String input = "Basic dGVzdA=";
        Output output = parser.parse(input);

    }

    @Test
    public void shouldBeInvalidCredentialValue_whenInputIsBasicMTIzNA() {

        exception.expect(InvalidHttpBasicAuthenException.class);
        exception.expectMessage("Invalid Credential value (Invalid basic authen format).");

        String input = "Basic MTIzNA==";
        Output output = parser.parse(input);

    }

    @Test
    public void shouldBeInvalidCredentialValue_whenInputIsBasicDGVzdDoxMjM0OjU2Nzg() {

        exception.expect(InvalidHttpBasicAuthenException.class);
        exception.expectMessage("Invalid Credential value (Invalid basic authen format).");

        String input = "Basic dGVzdDoxMjM0OjU2Nzg=";
        Output output = parser.parse(input);

    }

    @Test
    public void shouldBeOk_whenInputIsBasicDGVzdDoxMjM0NTY() {

        String input = "Basic dGVzdDoxMjM0NTY=";
        Output output = parser.parse(input);
        Output expected = new Output("test", "123456");

        assertThat(output.getUsername()).isEqualTo(expected.getUsername());
        assertThat(output.getPassword()).isEqualTo(expected.getPassword());
    }

    @Test
    public void shouldBeOk_whenInputIsBasicDGVzdDowMDAw_caseInsensitive() {

        String input = "basic dGVzdDowMDAw";
        Output output = parser.parse(input);
        Output expected = new Output("test", "0000");

        assertThat(output.getUsername()).isEqualTo(expected.getUsername());
        assertThat(output.getPassword()).isEqualTo(expected.getPassword());
    }
}
