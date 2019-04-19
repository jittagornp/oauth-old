/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security.hashing;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/07
 */
public class HmacSHA256HashingTest {

    private Hashing hashing;

    @Before
    public void withImplementation() {
        hashing = new HmacSHA256Hashing("abcd");
    }

    @Test
    public void hashHelloWorld() {
        byte[] input = "Hello World".getBytes();
        String output = hashing.hash(input);
        String expected = "64ce4e2287d26bff8c4679c60b80628112976b43caf25e6f8be1d49b14d32aa3";
        assertThat(output).isEqualTo(expected);
    }

}
