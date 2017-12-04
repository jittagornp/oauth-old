/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/29
 */
public class SHA256CheckSumTest {

    private CheckSum checkSum;

    @Before
    public void withImplementation() {
        checkSum = new SHA256CheckSum();
    }

    @Test
    public void hashHelloWorld() {
        byte[] input = "Hello World".getBytes();
        String output = checkSum.hash(input);
        String expected = "a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void matchesHellWorld() {
        byte[] input1 = "Hello World".getBytes();
        String input2 = "a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e";
        boolean output = checkSum.matches(input1, input2);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void hashPublicKey() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/key/public-key.pub")) {
            byte[] input = IOUtils.toByteArray(inputStream);
            String output = checkSum.hash(input);
            String expected = "d9e8ef7d19730b114907ce69263d2d2ad4ec1b84b891d5adeff8ab0dfea27b52";
            assertThat(output).isEqualTo(expected);
        }
    }

    @Test
    public void matchesPublicKey() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/key/public-key.pub")) {
            byte[] input1 = IOUtils.toByteArray(inputStream);
            String input2 = "d9e8ef7d19730b114907ce69263d2d2ad4ec1b84b891d5adeff8ab0dfea27b52";
            boolean output = checkSum.matches(input1, input2);
            boolean expected = true;
            assertThat(output).isEqualTo(expected);
        }
    }
}
