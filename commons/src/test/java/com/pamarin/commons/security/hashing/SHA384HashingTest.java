/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security.hashing;

import com.pamarin.commons.security.hashing.Hashing;
import com.pamarin.commons.security.hashing.SHA384Hashing;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/29
 */
public class SHA384HashingTest {

    private Hashing hashing;

    @Before
    public void withImplementation() {
        hashing = new SHA384Hashing();
    }

    @Test
    public void hashHelloWorld() {
        byte[] input = "Hello World".getBytes();
        String output = hashing.hash(input);
        String expected = "99514329186b2f6ae4a1329e7ee6c610a729636335174ac6b740f9028396fcc803d0e93863a7c3d90f86beee782f4f3f";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void matchesHellWorld() {
        byte[] input1 = "Hello World".getBytes();
        String input2 = "99514329186b2f6ae4a1329e7ee6c610a729636335174ac6b740f9028396fcc803d0e93863a7c3d90f86beee782f4f3f";
        boolean output = hashing.matches(input1, input2);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void hashPublicKey() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/key/public-key.pub")) {
            byte[] input = IOUtils.toByteArray(inputStream);
            String output = hashing.hash(input);
            String expected = "0eea0c8acf694a8bf86e5c11873bd9aa1b4ab630d27b0ee434c885e4e0d198936ee10770f306f66d8eb618daa584972a";
            assertThat(output).isEqualTo(expected);
        }
    }

    @Test
    public void matchesPublicKey() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/key/public-key.pub")) {
            byte[] input1 = IOUtils.toByteArray(inputStream);
            String input2 = "0eea0c8acf694a8bf86e5c11873bd9aa1b4ab630d27b0ee434c885e4e0d198936ee10770f306f66d8eb618daa584972a";
            boolean output = hashing.matches(input1, input2);
            boolean expected = true;
            assertThat(output).isEqualTo(expected);
        }
    }
}
