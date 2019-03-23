/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.generator;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jitta
 */
public class DefaultUUIDGeneratorTest {

    private UUIDGenerator generator;

    @Before
    public void before() {
        generator = new DefaultUUIDGenerator();
    }

    @Test
    public void test() {
        String output = generator.generate();
        int expected = 36;
        assertThat(output.length()).isEqualTo(expected);
    }
}
