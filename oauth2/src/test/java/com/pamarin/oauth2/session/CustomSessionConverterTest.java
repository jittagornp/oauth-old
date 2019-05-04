/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import static com.pamarin.oauth2.session.CustomSession.Attribute.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jitta
 */
public class CustomSessionConverterTest {

    private CustomSessionConverter customSessionConverter;

    @Before
    public void before() {
        customSessionConverter = new DefaultCustomSessionConverter();
    }

    @Test
    public void shouldBeNull_whenEntriesIsNull() {
        Set<Map.Entry<String, Object>> input = null;
        CustomSession output = customSessionConverter.entriesToSession(input);
        CustomSession expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenEntriesIsEmptySet() {
        Set<Map.Entry<String, Object>> input = Collections.emptySet();
        CustomSession output = customSessionConverter.entriesToSession(input);
        CustomSession expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeOk() {
        Map<String, Object> input = new HashMap<>();
        input.put(SESSION_ID, "abcd");
        input.put(CREATION_TIME, 1000L);
        input.put(MAX_INACTIVE_INTERVAL, null);
        input.put(LAST_ACCESSED_TIME, System.currentTimeMillis());
        input.put(EXPIRATION_TIME, System.currentTimeMillis());
        input.put(AGENT_ID, "12345");
        input.put(USER_ID, "12345");
        input.put(IP_ADDRESS, "0.0.0.0");
        input.put("unknownAttributeName", "xxx");
        input.put(ATTRIBUTES, new Object());
        input.put(ATTRIBUTES + ":name", "jittagornp");

        CustomSession output = customSessionConverter.entriesToSession(input.entrySet());

        assertThat(output.getId()).isEqualTo(input.get(SESSION_ID));
        assertThat(output.getSessionId()).isEqualTo(input.get(SESSION_ID));
        assertThat(output.getCreationTime()).isEqualTo(input.get(CREATION_TIME));
        assertThat(output.getMaxInactiveInterval()).isEqualTo(0);
        assertThat(output.getMaxInactiveIntervalInSeconds()).isEqualTo(0);
        assertThat(output.getLastAccessedTime()).isEqualTo(input.get(LAST_ACCESSED_TIME));
        assertThat(output.getExpirationTime()).isEqualTo(input.get(EXPIRATION_TIME));
        assertThat(output.getAgentId()).isEqualTo(input.get(AGENT_ID));
        assertThat(output.getUserId()).isEqualTo(input.get(USER_ID));
        assertThat(output.getIpAddress()).isEqualTo(input.get(IP_ADDRESS));
        assertThat(output.getAttributeNames().size()).isEqualTo(1);
        assertThat((String) output.getAttribute("name")).isEqualTo("jittagornp");
    }
}
