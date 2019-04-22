/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import com.pamarin.commons.util.ObjectEquals;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author jitta
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = UserSession.COLLECTION_NAME)
public class UserSession implements Serializable {

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
