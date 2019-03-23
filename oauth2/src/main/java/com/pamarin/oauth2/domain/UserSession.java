/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jitta
 */
@Getter
@Setter
@Entity
@Table(name = UserSession.TABLE_NAME)
public class UserSession implements Serializable {

    public static final String TABLE_NAME = "user_session";

    @Id
    private String id;

    @Column(name = "session_id", nullable = false, unique = true)
    private String sessionId;

    @Column(name = "creation_time")
    private Long creationTime;

    @Column(name = "max_inactive_interval")
    private Integer maxInactiveInterval;

    @Column(name = "last_accessed_time")
    private Long lastAccessedTime;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "agent_id")
    private String agentId;

    @Column(name = "ip_address")
    private String ipAddress;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserSession other = (UserSession) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
