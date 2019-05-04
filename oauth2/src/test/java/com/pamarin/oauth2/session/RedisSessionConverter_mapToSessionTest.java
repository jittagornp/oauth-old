/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import static com.pamarin.oauth2.session.CustomSession.Attribute.SESSION_ID;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jitta
 */
public class RedisSessionConverter_mapToSessionTest {

    private RedisSessionConverter converter;

    @Before
    public void before() {
        converter = new DefaultRedisSessionConverter();
    }

    @Test
    public void shouldBeNull_whenInputIsNull() {
        Map<Object, Object> input = null;
        CustomSession output = converter.mapToSession(input);
        CustomSession expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenInputIsEmptyMap() {
        Map<Object, Object> input = new HashMap<>();
        CustomSession output = converter.mapToSession(input);
        CustomSession expected = null;
        assertThat(output).isEqualTo(expected);
    }
    
    @Test
    public void shouldBeOk() {
        Map<Object, Object> input = new HashMap<>();
        input.put(SESSION_ID, "abcd");
        CustomSession output = converter.mapToSession(input);
        assertThat(output.getId()).isEqualTo(input.get(SESSION_ID));
    }
}
