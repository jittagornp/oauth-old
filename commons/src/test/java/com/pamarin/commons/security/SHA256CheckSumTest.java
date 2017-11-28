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
        String expected = "pZGm1Av0IEBKARczz7exkNYsZb8LzaMrV7J32a2fFG4=";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void matchesHellWorld() {
        byte[] input1 = "Hello World".getBytes();
        String input2 = "pZGm1Av0IEBKARczz7exkNYsZb8LzaMrV7J32a2fFG4=";
        boolean output = checkSum.matches(input1, input2);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void hashPublicKey() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/key/public-key.pub")) {
            byte[] input = IOUtils.toByteArray(inputStream);
            String output = checkSum.hash(input);
            String expected = "2ejvfRlzCxFJB85pJj0tKtTsG4S4kdWt7/irDf6ie1I=";
            assertThat(output).isEqualTo(expected);
        }
    }

    @Test
    public void matchesPublicKey() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/key/public-key.pub")) {
            byte[] input1 = IOUtils.toByteArray(inputStream);
            String input2 = "2ejvfRlzCxFJB85pJj0tKtTsG4S4kdWt7/irDf6ie1I=";
            boolean output = checkSum.matches(input1, input2);
            boolean expected = true;
            assertThat(output).isEqualTo(expected);
        }
    }
}
