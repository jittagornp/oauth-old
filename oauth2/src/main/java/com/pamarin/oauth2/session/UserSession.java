/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import static com.pamarin.commons.util.DateConverterUtils.convert2Timestamp;
import com.pamarin.commons.util.ObjectEquals;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.session.ExpiringSession;

/**
 *
 * @author jitta
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@Document(collection = UserSession.COLLECTION_NAME)
public class UserSession implements ExpiringSession, Serializable {

    public static final String COLLECTION_NAME = "user_session";

    @Id
    private String id;

    @Indexed
    private String sessionId;

    private long creationTime;

    private long expirationTime;

    private int maxInactiveInterval;

    private long lastAccessedTime;

    private String userId;

    private String agentId;

    private String ipAddress;

    private final Map<String, Object> sessionAttrs;

    public UserSession() {
        this.id = UUID.randomUUID().toString();
        this.sessionAttrs = new HashMap<>();
        this.creationTime = convert2Timestamp(LocalDateTime.now());
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
        return isExpired(convert2Timestamp(LocalDateTime.now()));
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
        return (T) this.sessionAttrs.get(attributeName);
    }

    @Override
    public Set<String> getAttributeNames() {
        return new HashSet<>(this.sessionAttrs.keySet());
    }

    @Override
    public void setAttribute(String attributeName, Object attributeValue) {
        if (attributeValue == null) {
            removeAttribute(attributeName);
        } else {
            this.sessionAttrs.put(attributeName, attributeValue);
        }
    }

    @Override
    public void removeAttribute(String attributeName) {
        this.sessionAttrs.remove(attributeName);
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
                .equals(obj, (origin, other)
                        -> Objects.equals(origin.getId(), other.getId())
                || Objects.equals(origin.getSessionId(), other.getSessionId())
                );
    }
}
