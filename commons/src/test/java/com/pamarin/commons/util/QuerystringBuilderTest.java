/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/22
 */
public class QuerystringBuilderTest {

    @Test
    public void shouldBeEmptyString_whenEmptyParameter() {
        QuerystringBuilder input = new QuerystringBuilder();
        String output = input.build();
        String expected = "";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeEmptyString_whenResponseTypeIsNull() {
        QuerystringBuilder input = new QuerystringBuilder()
                .addParameter("response_type", null);
        String output = input.build();
        String expected = "";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeResponseTypeIsCode() {
        QuerystringBuilder input = new QuerystringBuilder()
                .addParameter("response_type", "code");
        String output = input.build();
        String expected = "response_type=code";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeResponseTypeIsCode_whenScopeIsNull() {
        QuerystringBuilder input = new QuerystringBuilder()
                .addParameter("response_type", "code")
                .addParameter("scope", null);
        String output = input.build();
        String expected = "response_type=code";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeResponseTypeIsCode_andScopeIsRead() {
        QuerystringBuilder input = new QuerystringBuilder()
                .addParameter("response_type", "code")
                .addParameter("scope", "read");
        String output = input.build();
        String expected = "response_type=code&scope=read";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeResponseTypeIsCode_andScopeIsRead_andRedirecUriIsEncodedHttpLocalhost() {
        QuerystringBuilder input = new QuerystringBuilder()
                .addParameter("response_type", "code")
                .addParameter("scope", "read")
                .addParameter("redirect_uri", "http://localhost/callback");
        String output = input.build();
        String expected = "response_type=code&scope=read&redirect_uri=http%3A%2F%2Flocalhost%2Fcallback";
        assertThat(output).isEqualTo(expected);
    }
    
    @Test
    public void shouldBeResponseTypeIsCode_andScopeIsRead_andRedirecUriIsEncodedHttpLocalhostWithQuerystring() {
        QuerystringBuilder input = new QuerystringBuilder()
                .addParameter("response_type", "code")
                .addParameter("scope", "read")
                .addParameter("redirect_uri", "http://localhost/callback?q=keyword");
        String output = input.build();
        String expected = "response_type=code&scope=read&redirect_uri=http%3A%2F%2Flocalhost%2Fcallback%3Fq%3Dkeyword";
        assertThat(output).isEqualTo(expected);
    }

}
