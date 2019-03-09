/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.util;

import static com.pamarin.commons.util.ClassUtils.isPrivateConstructor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author jitta
 */
public class Base64UtilsTest {

    @Test
    public void shouldBePrivateConstructor() {
        assertTrue(isPrivateConstructor(Base64Utils.class));
    }

    //encode
    //==========================================================================
    @Test
    public void shouldBeNull_whenEncodeNullString() {
        String input = null;
        String output = Base64Utils.encode(input);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenEncodeNullByte() {
        byte[] input = null;
        String output = Base64Utils.encode(input);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeOk_whenEncodeHelloString() {
        String input = "hello";
        String output = Base64Utils.encode(input);
        String expected = "aGVsbG8=";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeOk_whenEncodeHelloByte() {
        byte[] input = "hello".getBytes();
        String output = Base64Utils.encode(input);
        String expected = "aGVsbG8=";
        assertThat(output).isEqualTo(expected);
    }

    //decode
    //==========================================================================
    @Test
    public void shouldBeNull_whenDecodeNullString() {
        String input = null;
        String output = Base64Utils.decode(input);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenDecodeNullByte() {
        byte[] input = null;
        String output = Base64Utils.decode(input);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenDecodeInvalidString() {
        String input = "aGVsbG8=xxxx";
        String output = Base64Utils.decode(input);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenDecodeInvalidByte() {
        byte[] input = "aGVsbG8=xxxx".getBytes();
        String output = Base64Utils.decode(input);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeOk_whenDecodeHelloString() {
        String input = "aGVsbG8=";
        String output = Base64Utils.decode(input);
        String expected = "hello";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeOk_whenDecodeHelloByte() {
        byte[] input = "aGVsbG8=".getBytes();
        String output = Base64Utils.decode(input);
        String expected = "hello";
        assertThat(output).isEqualTo(expected);
    }
}
