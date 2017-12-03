/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.util;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
public class HttpAuthorizeBearerParserTest {

    private HttpAuthorizeBearerParser parser;

    @Before
    public void withImplemetation() {
        parser = new DefaultHttpAuthorizeBearerParser();

        ReflectionTestUtils.setField(
                parser,
                "parser",
                new DefaultHttpAuthorizationParser()
        );
    }
    
    @Test
    public void shouldBeOk(){
        String input = "Bearer xyz";
        String output = parser.parse(input);
        String expected = "xyz";
        assertThat(output).isEqualTo(expected);
    }

}
