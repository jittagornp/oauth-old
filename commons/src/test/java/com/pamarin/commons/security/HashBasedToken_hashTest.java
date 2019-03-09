/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.security.hashing.HmacSHA384Hashing;
import java.time.LocalDateTime;
import java.time.Month;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/04
 */
public class HashBasedToken_hashTest {

    private static final Logger LOG = LoggerFactory.getLogger(HashBasedToken_hashTest.class);

    private static final String HASHBASED_KEY = "xyz";

    private HashBasedToken hashBasedToken;

    @Before
    public void withImplementation() {
        hashBasedToken = new DefaultHashBasedToken(new HmacSHA384Hashing(HASHBASED_KEY));
    }

    /*
     * b98e21b4-ce2a-11e7-abc4-cec278b6b50a:4102419600000:09b78ce0be27cffd785059c46b77a342c93685467cfc5ca8708c766381cafa3a
     */
    @Test
    public void shouldBeOk() {
        LocalDateTime expires = LocalDateTime.of(5000, Month.JANUARY, 1, 0, 0, 0, 0);
        UserDetails credential = DefaultUserDetails.builder()
                .username("b98e21b4-ce2a-11e7-abc4-cec278b6b50a")
                .password("$2a$10$2SLqTL7.Ug9cyfRPFwxQeemMA4SeB9MymLjRl4RGR0h7aHwLDy7qC")
                .build();
        String output = hashBasedToken.hash(credential, expires);
        LOG.debug("token => {}", output);
        assertThat(output).isNotNull();
    }
}
