/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import static com.pamarin.commons.util.ClassUtils.isPrivateConstructor;
import static com.pamarin.commons.util.DateConverterUtils.convert2Timestamp;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author jitta
 */
public class CustomSessionTest {

    @Test
    public void shouldBePrivateConstructor_forCustomSessionAttribute() {
        assertTrue(isPrivateConstructor(CustomSession.Attribute.class));
    }

    @Test
    public void maxInactiveIntervalShouldBe1800_whenNewInstance() {
        CustomSession session = new CustomSession();

        int maxInactiveInterval = session.getMaxInactiveIntervalInSeconds();
        int expected = 1800;
        assertThat(maxInactiveInterval).isEqualTo(expected);
    }

    @Test
    public void idShouldBeUUID_whenNewInstance() {
        CustomSession session = new CustomSession();
        String id = session.getId();
        int expected = 36;
        assertThat(id.length()).isEqualTo(expected);
    }

    @Test
    public void idAndSessionIdShouldBeSameValue_whenNewInstance() {
        CustomSession session = new CustomSession();
        String id = session.getId();
        String sessionId = session.getSessionId();
        assertThat(id).isEqualTo(sessionId);
    }

    @Test
    public void creationTimeAndLastAccesedTimeShouldBeSameValue_whenNewInstance() {
        CustomSession session = new CustomSession();
        long creationTime = session.getCreationTime();
        long lastAccessedTime = session.getLastAccessedTime();
        assertThat(creationTime).isEqualTo(lastAccessedTime);
    }

    @Test
    public void attributesShouldBeEmpty_whenNewInstance() {
        CustomSession session = new CustomSession();
        Map<String, Object> attributes = session.getAttributes();
        Map<String, Object> empty = new HashMap<>();
        assertThat(attributes).isEqualTo(empty);
    }

    @Test
    public void shouldBeNotExpire_whenNewInstance() {
        CustomSession session = new CustomSession();
        boolean output = session.isExpired();
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNotExpire_whenMaxInactiveIntervalIsNegativeValue() {
        CustomSession session = new CustomSession(-1);
        boolean output = session.isExpired();
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeExpired_whenNotAccessMoreThanOrEqualsMaxInactiveInterval() {
        int maxInactiveInterval = 5;
        CustomSession session = new CustomSession();
        LocalDateTime lastAccessTime = now().minusSeconds(maxInactiveInterval);
        session.setLastAccessedTime(convert2Timestamp(lastAccessTime));
        session.setMaxInactiveInterval(maxInactiveInterval);

        boolean output = session.isExpired();
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeSameValue_whenSetAndGetAttribute() {
        String attributeName = "name";
        String attributeValue = "jittagornp";

        CustomSession session = new CustomSession();
        session.setAttribute(attributeName, attributeValue);
        assertThat(session.getAttributeNames().size()).isEqualTo(1);

        String output = session.getAttribute(attributeName);
        String expected = attributeValue;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenSetAndSetNullAttribute() {
        String attributeName = "name";
        String attributeValue = "jittagornp";

        CustomSession session = new CustomSession();
        session.setAttribute(attributeName, attributeValue);
        assertThat(session.getAttributeNames().size()).isEqualTo(1);

        session.setAttribute(attributeName, null);
        assertThat(session.getAttributeNames().size()).isEqualTo(0);

        String output = session.getAttribute(attributeName);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenSetAndRemoveAttribute() {
        String attributeName = "name";
        String attributeValue = "jittagornp";

        CustomSession session = new CustomSession();
        session.setAttribute(attributeName, attributeValue);
        session.removeAttribute(attributeName);

        String output = session.getAttribute(attributeName);
        String expected = null;
        assertThat(output).isEqualTo(expected);
        assertThat(session.getAttributeNames().size()).isEqualTo(0);
    }
}
