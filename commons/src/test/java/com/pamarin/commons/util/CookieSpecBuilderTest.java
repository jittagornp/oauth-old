/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.util;

import com.pamarin.commons.util.CookieSpecBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/19
 */
public class CookieSpecBuilderTest {

    @Test
    public void shouldBeOk_whenHaveOnlyKeyValue() {
        String output = new CookieSpecBuilder("X-CSRF-Token", "xyz").build();
        String expected = "X-CSRF-Token=xyz; Path=/; HttpOnly;";
        assertThat(output).isEqualTo(expected);
    }
    
    @Test
    public void shouldBeOk_whenSetHttpOnlyFalse() {
        String output = new CookieSpecBuilder("X-CSRF-Token", "xyz")
                .setHttpOnly(Boolean.FALSE)
                .build();
        String expected = "X-CSRF-Token=xyz; Path=/;";
        assertThat(output).isEqualTo(expected);
    }
    
    @Test
    public void shouldBeOk_whenSetPathIsLogin() {
        String output = new CookieSpecBuilder("X-CSRF-Token", "xyz")
                .setHttpOnly(Boolean.TRUE)
                .setPath("/login")
                .build();
        String expected = "X-CSRF-Token=xyz; Path=/login; HttpOnly;";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeOk_whenSetSecureTrue() {
        String output = new CookieSpecBuilder("X-CSRF-Token", "xyz")
                .setHttpOnly(Boolean.TRUE)
                .setPath("/login")
                .setSecure(Boolean.TRUE)
                .build();
        String expected = "X-CSRF-Token=xyz; Path=/login; HttpOnly; Secure;";
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
        String expected = "X-CSRF-Token=xyz; Path=/login; HttpOnly; SameSite=Strict; Secure;";
        assertThat(output).isEqualTo(expected);
    }

}
