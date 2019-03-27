/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import java.util.Arrays;
import java.util.Collection;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author jitta
 */
public class DefaultUserDetailsTest {

    private UserDetails userDetails;

    @Before
    public void before() {
        userDetails = DefaultUserDetails.builder()
                .username("jittagornp")
                .password("test")
                .authorities(Arrays.asList("admin"))
                .build();
    }

    @Test
    public void shouldBeSingleElement() {
        Collection<? extends GrantedAuthority> output = userDetails.getAuthorities();
        int expected = 1;
        assertThat(output.size()).isEqualTo(expected);
        assertThat(output.iterator().next().getAuthority()).isEqualTo("admin");
    }

    @Test
    public void shouldBeTrue_whenIsAccountNonExpired() {
        boolean output = userDetails.isAccountNonExpired();
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenIsAccountNonLocked() {
        boolean output = userDetails.isAccountNonLocked();
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenIsCredentialsNonExpired() {
        boolean output = userDetails.isCredentialsNonExpired();
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenIsEnabled() {
        boolean output = userDetails.isEnabled();
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeMinus1405401465_whenCallHashCode() {
        int output = userDetails.hashCode();
        int expected = -1405401465;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeFalse_whenNotEqualsUsername() {
        UserDetails obj = DefaultUserDetails.builder()
                .username("admin")
                .build();
        boolean output = userDetails.equals(obj);
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenEqualsUsername() {
        UserDetails obj = DefaultUserDetails.builder()
                .username("jittagornp")
                .build();
        boolean output = userDetails.equals(obj);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }
}
