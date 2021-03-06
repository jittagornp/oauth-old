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
public class DefaultIdGeneratorTest {

    private IdGenerator generator;

    @Before
    public void before() {
        generator = new DefaultIdGenerator();
    }

    @Test
    public void test() {
        String output = generator.generate();
        int expected = 24;
        assertThat(output.length()).isEqualTo(expected);
    }
}
