/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import static com.pamarin.oauth2.session.CustomSession.Attribute.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author jitta
 */
public class RedisSessionConverter_sessionToMapTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private RedisSessionConverter converter;

    @Before
    public void before() {
        converter = new DefaultRedisSessionConverter();
    }

    @Test
    public void shouldBeRequireSession_whenInputIsNull() {

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("require session.");

        CustomSession input = null;
        converter.sessionToMap(input);
    }

    @Test
    public void shouldBeOk() {
        CustomSession input = new CustomSession();
        input.setId("abcd");
        input.setCreationTime(System.currentTimeMillis());
        input.setLastAccessedTime(System.currentTimeMillis());
        input.setMaxInactiveInterval(1800);
        input.setUserId("12345");
        input.setAttribute("name", "jittagornp");

        Map<String, Object> output = converter.sessionToMap(input);
        
        assertThat(output.get(SESSION_ID)).isEqualTo(input.getId());
        assertThat(output.get(CREATION_TIME)).isEqualTo(input.getCreationTime());
        assertThat(output.get(MAX_INACTIVE_INTERVAL)).isEqualTo(input.getMaxInactiveIntervalInSeconds());
        assertThat(output.get(LAST_ACCESSED_TIME)).isEqualTo(input.getLastAccessedTime());
        assertThat(output.get(EXPIRATION_TIME)).isEqualTo(input.getExpirationTime());
        assertThat(output.get(USER_ID)).isEqualTo(input.getUserId());
        assertThat(output.get(ATTRIBUTES + ":name")).isEqualTo(input.getAttribute("name"));
        assertThat(output.size()).isEqualTo(7);
    }
}
