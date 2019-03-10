/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security.hashing;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jitta
 */
public class ShortHashingTest {

    private ShortHashing shortHashing;

    @Before
    public void before() {

        shortHashing = new ShortHashing(new SHA384Hashing(), 32);

    }

    @Test
    public void sholdBe32Characters() {
        String input = "Hello World";
        String output = shortHashing.hash(input.getBytes());
        System.out.println("output => " + output);
        assertTrue(shortHashing.matches(input.getBytes(), output));
    }

}
