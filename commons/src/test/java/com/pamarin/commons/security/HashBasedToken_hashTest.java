/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import java.time.LocalDateTime;
import java.time.Month;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/04
 */
public class HashBasedToken_hashTest {

    private static final String HASHBASED_KEY = "xyz";

    private HashBasedToken hashBasedToken;

    @Before
    public void withImplementation() {
        hashBasedToken = new DefaultHashBasedToken(HASHBASED_KEY, new SHA256CheckSum());
    }

    /*
     * b98e21b4-ce2a-11e7-abc4-cec278b6b50a:4102419600000:CbeM4L4nz/14UFnEa3ejQsk2hUZ8/FyocIx2Y4HK+jo=
     */
    @Test
    public void shouldBeOk() {
        LocalDateTime expires = LocalDateTime.of(2100, Month.JANUARY, 1, 0, 0, 0, 0);
        UserDetails credential = DefaultUserDetails.builder()
                .username("b98e21b4-ce2a-11e7-abc4-cec278b6b50a")
                .password("$2a$10$2SLqTL7.Ug9cyfRPFwxQeemMA4SeB9MymLjRl4RGR0h7aHwLDy7qC")
                .build();
        String output = hashBasedToken.hash(credential, expires);
        assertThat(output).isNotNull();
    }
}
