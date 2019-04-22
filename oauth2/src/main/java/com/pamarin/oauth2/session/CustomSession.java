/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import static com.pamarin.commons.util.DateConverterUtils.convert2Timestamp;
import com.pamarin.commons.util.ObjectEquals;
import java.io.Serializable;
import static java.time.LocalDateTime.now;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import org.springframework.session.ExpiringSession;

/**
 *
 * @author jitta
 */
@Getter
@Setter
public final class CustomSession implements ExpiringSession, Serializable {

    public static class Attribute {

        //follow session spec
        public static final String SESSION_ID = "sessionId"; //is id
        public static final String CREATION_TIME = "creationTime";
        public static final String LAST_ACCESSED_TIME = "lastAccessedTime";
        public static final String MAX_INACTIVE_INTERVAL = "maxInactiveInterval";

        //additional
        public static final String EXPIRATION_TIME = "expirationTime";
        public static final String AGENT_ID = "agentId";
        public static final String USER_ID = "userId";
        public static final String IP_ADDRESS = "ipAddress";
        public static final String ATTRIBUTES = "attrs";

        private Attribute() {

        }
    }

    private String id;

    private String sessionId;

    private long creationTime;

    private long expirationTime;

    private int maxInactiveInterval;

    private long lastAccessedTime;

    private String userId;

    private String agentId;

    private String ipAddress;

    private final Map<String, Object> attributes;

    public CustomSession() {
        this(1800);
    }

    public CustomSession(int maxInactiveInterval) {
        this.id = UUID.randomUUID().toString();
        this.sessionId = this.id;
        this.attributes = new HashMap<>();
        this.creationTime = convert2Timestamp(now());
        this.lastAccessedTime = this.creationTime;
        this.maxInactiveInterval = maxInactiveInterval;
        this.expirationTime = this.lastAccessedTime + TimeUnit.SECONDS.toMillis(this.maxInactiveInterval);
    }

    @Override
    public void setMaxInactiveIntervalInSeconds(int interval) {
        this.maxInactiveInterval = interval;
    }

    @Override
    public int getMaxInactiveIntervalInSeconds() {
        return this.maxInactiveInterval;
    }

    @Override
    public boolean isExpired() {
        return isExpired(convert2Timestamp(now()));
    }

    private boolean isExpired(long now) {
        if (this.maxInactiveInterval < 0) {
            return false;
        }
        return now - TimeUnit.SECONDS
                .toMillis(this.maxInactiveInterval) >= this.lastAccessedTime;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String attributeName) {
        return (T) this.attributes.get(attributeName);
    }

    @Override
    public Set<String> getAttributeNames() {
        return new HashSet<>(this.attributes.keySet());
    }

    @Override
    public void setAttribute(String attributeName, Object attributeValue) {
        if (attributeValue == null) {
            removeAttribute(attributeName);
        } else {
            this.attributes.put(attributeName, attributeValue);
        }
    }

    @Override
    public void removeAttribute(String attributeName) {
        this.attributes.remove(attributeName);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return ObjectEquals.of(this)
                .equals(obj, (origin, other) -> Objects.equals(origin.getId(), other.getId()));
    }

}
