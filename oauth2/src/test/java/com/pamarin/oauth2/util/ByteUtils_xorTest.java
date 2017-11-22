/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.util;

import java.lang.reflect.Constructor;
import static java.lang.reflect.Modifier.isPrivate;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/18
 */
public class ByteUtils_xorTest {

    @Test
    public void shouldBePrivateConstructor() throws NoSuchMethodException {
        Constructor<ByteUtils> input = ByteUtils.class.getDeclaredConstructor();
        boolean output = isPrivate(input.getModifiers());
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    /*
     * A | B | answer 
     * --------------
     * 0 | 0 | 0 
     * 0 | 1 | 1 
     * 1 | 0 | 1 
     * 1 | 1 | 0
     */
    @Test
    public void shouldBe00000000() {
        byte[] input1 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        byte[] input2 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        byte[] output = ByteUtils.xor(input1, input2);
        byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe11111111() {
        byte[] input1 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        byte[] input2 = new byte[]{1, 1, 1, 1, 1, 1, 1, 1};
        byte[] output = ByteUtils.xor(input1, input2);
        byte[] expected = new byte[]{1, 1, 1, 1, 1, 1, 1, 1};
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe00000000_whenOneAll() {
        byte[] input1 = new byte[]{1, 1, 1, 1, 1, 1, 1, 1};
        byte[] input2 = new byte[]{1, 1, 1, 1, 1, 1, 1, 1};
        byte[] output = ByteUtils.xor(input1, input2);
        byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe00001111() {
        byte[] input1 = new byte[]{1, 1, 1, 1};
        byte[] input2 = new byte[]{1, 1, 1, 1, 1, 1, 1, 1};
        byte[] output = ByteUtils.xor(input1, input2);
        byte[] expected = new byte[]{0, 0, 0, 0, 1, 1, 1, 1};
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe11110000() {
        byte[] input1 = new byte[]{1, 1, 1, 1, 0, 0, 0, 0};
        byte[] input2 = new byte[]{0, 0, 0, 0};
        byte[] output = ByteUtils.xor(input1, input2);
        byte[] expected = new byte[]{1, 1, 1, 1, 0, 0, 0, 0};
        assertThat(output).isEqualTo(expected);
    }
}
