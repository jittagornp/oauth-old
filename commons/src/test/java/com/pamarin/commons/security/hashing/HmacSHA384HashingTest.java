/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security.hashing;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/07
 */
public class HmacSHA384HashingTest {

    private Hashing hashing;

    @Before
    public void withImplementation() {
        hashing = new HmacSHA384Hashing("abcd");
    }

    @Test
    public void hashHelloWorld() {
        byte[] input = "Hello World".getBytes();
        String output = hashing.hash(input);
        String expected = "51208494f156e18f38109674d9856b896cc06714f07cc5e0b7d8c63b41824660b2ff9bbdb9549fb53d369c95507a1e3a";
        assertThat(output).isEqualTo(expected);
    }

}
