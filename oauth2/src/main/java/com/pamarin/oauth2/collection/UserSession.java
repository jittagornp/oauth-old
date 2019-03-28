/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.collection;

import com.pamarin.commons.util.ObjectEquals;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @author jitta
 */
@Getter
@Setter
@Builder
@Document(collection = UserSession.COLLECTION_NAME)
public class UserSession implements Serializable {

    public static final String COLLECTION_NAME = "user_session";

    @Id
    private String id;

    @Indexed
    @Field("session_id")
    private String sessionId;

    @Field("creation_time")
    private Long creationTime;

    @Field("max_inactive_interval")
    private Integer maxInactiveInterval;

    @Field("last_accessed_time")
    private Long lastAccessedTime;

    @Field("user_id")
    private String userId;

    @Field("agent_id")
    private String agentId;

    @Field("ip_address")
    private String ipAddress;

    public UserSession() {
    }

    public UserSession(String id, String sessionId, Long creationTime, Integer maxInactiveInterval, Long lastAccessedTime, String userId, String agentId, String ipAddress) {
        this.id = id;
        this.sessionId = sessionId;
        this.creationTime = creationTime;
        this.maxInactiveInterval = maxInactiveInterval;
        this.lastAccessedTime = lastAccessedTime;
        this.userId = userId;
        this.agentId = agentId;
        this.ipAddress = ipAddress;
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
                .equals(obj, (origin, other) -> {
                    return Objects.equals(origin.getId(), other.getId())
                            || Objects.equals(origin.getSessionId(), other.getSessionId());
                });
    }
}
