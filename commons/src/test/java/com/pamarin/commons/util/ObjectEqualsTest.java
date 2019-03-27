/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 *
 * @author jitta
 */
public class ObjectEqualsTest {

    @Getter
    @Setter
    @Builder
    public static class User {

        private String username;

    }

    @Getter
    @Setter
    @Builder
    public static class Client {

        private String name;

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldBeThrowIllegalArgumentException_whenInput1IsNull() {
        User input1 = null;
        User input2 = null;
        boolean output = ObjectEquals.of(input1).equals(input2, null);
    }

    @Test
    public void shouldBeTrue_whenInput1IsInput2() {
        User input1 = User.builder().build();
        User input2 = input1;
        boolean output = ObjectEquals.of(input1).equals(input2, null);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeFalse_whenInput2IsNull() {
        User input1 = User.builder().build();
        User input2 = null;
        boolean output = ObjectEquals.of(input1).equals(input2, null);
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeFalse_whenInput1IsUserClass_andInput2IsClientClass() {
        User input1 = User.builder().build();
        Client input2 = Client.builder().build();
        boolean output = ObjectEquals.of(input1).equals(input2, null);
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeFalse_whenNotEqualsAttribute() {
        User input1 = User.builder().username("jittagornp").build();
        User input2 = User.builder().username("admin").build();
        boolean output = ObjectEquals.of(input1).equals(input2, (origin, other) -> Objects.equals(origin.getUsername(), other.getUsername()));
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue() {
        User input1 = User.builder().username("jittagornp").build();
        User input2 = User.builder().username("jittagornp").build();
        boolean output = ObjectEquals.of(input1).equals(input2, (origin, other) -> Objects.equals(origin.getUsername(), other.getUsername()));
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }
}
