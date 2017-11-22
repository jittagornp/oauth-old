/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.security;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/15
 */
public class AuthenticityTokenTest {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticityTokenTest.class);

    private void test(int size) {
        AuthenticityToken authenticityToken = new DefaultAuthenticityToken(size);
        AuthenticityToken.RandomOutput input = authenticityToken.random();
        LOG.debug("random output => {}", input);
        boolean output = authenticityToken.matches(input.getToken(), input.getAuthenticityToken());
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    private void testDecode(String input) {
        AuthenticityToken authenticityToken = new DefaultAuthenticityToken(11);
        String output = authenticityToken.decode(input);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenInputIsNull() {
        testDecode(null);
    }

    @Test
    public void shouldBeNull_whenInputIsEmptyString() {
        testDecode("");
    }

    @Test
    public void shouldBeNull_whenInputIsMTIzNDU2() {
        testDecode("MTIzNDU2");
    }

    @Test
    public void shouldBeOk_whenInputSizeIs7() {
        test(7);
    }

    @Test
    public void shouldBeOk_whenInputSizeIs9() {
        test(9);
    }

    @Test
    public void shouldBeOk_whenInputSizeIs11() {
        test(11);
    }

    @Test
    public void shouldBeOk_whenInputSizeIs13() {
        test(13);
    }

    @Test
    public void shouldBeOk_whenInputSizeIs15() {
        test(15);
    }

    @Test
    public void shouldBeOk_whenInputSizeIs17() {
        test(17);
    }

    @Test
    public void shouldBeOk_whenInputSizeIs19() {
        test(19);
    }

}
