/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.util;

import java.time.LocalDateTime;
import java.time.Month;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/19
 */
public class CookieSpecBuilderTest {

    @Test
    public void shouldBeOk_whenHaveOnlyKeyValue() {
        String output = new CookieSpecBuilder("X-CSRF-Token", "xyz").build();
        String expected = "X-CSRF-Token=xyz; Path=/; HttpOnly";
        assertThat(output).isEqualTo(expected);
    }
    
    @Test
    public void shouldBeOk_whenSetHttpOnlyFalse() {
        String output = new CookieSpecBuilder("X-CSRF-Token", "xyz")
                .setHttpOnly(Boolean.FALSE)
                .build();
        String expected = "X-CSRF-Token=xyz; Path=/";
        assertThat(output).isEqualTo(expected);
    }
    
    @Test
    public void shouldBeOk_whenSetPathIsLogin() {
        String output = new CookieSpecBuilder("X-CSRF-Token", "xyz")
                .setHttpOnly(Boolean.TRUE)
                .setPath("/login")
                .build();
        String expected = "X-CSRF-Token=xyz; Path=/login; HttpOnly";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeOk_whenSetSecureTrue() {
        String output = new CookieSpecBuilder("X-CSRF-Token", "xyz")
                .setHttpOnly(Boolean.TRUE)
                .setPath("/login")
                .setSecure(Boolean.TRUE)
                .build();
        String expected = "X-CSRF-Token=xyz; Path=/login; HttpOnly; Secure";
        assertThat(output).isEqualTo(expected);
    }
    
    @Test
    public void shouldBeOk_whenSameSiteStrict() {
        String output = new CookieSpecBuilder("X-CSRF-Token", "xyz")
                .setHttpOnly(Boolean.TRUE)
                .setPath("/login")
                .setSecure(Boolean.TRUE)
                .sameSiteStrict()
                .build();
        String expected = "X-CSRF-Token=xyz; Path=/login; HttpOnly; SameSite=Strict; Secure";
        assertThat(output).isEqualTo(expected);
    }
    
     @Test
    public void shouldBeOk_whenExpires() {
        String output = new CookieSpecBuilder("X-CSRF-Token", "xyz")
                .setHttpOnly(Boolean.TRUE)
                .setPath("/login")
                .setSecure(Boolean.TRUE)
                .sameSiteStrict()
                .setExpires(LocalDateTime.of(2020, Month.JANUARY, 1, 12, 30))
                .build();
        String expected = "X-CSRF-Token=xyz; Path=/login; HttpOnly; SameSite=Strict; Secure; Expires=Wed, 01 Jan 2020 12:30:00 GMT";
        assertThat(output).isEqualTo(expected);
    }

}
