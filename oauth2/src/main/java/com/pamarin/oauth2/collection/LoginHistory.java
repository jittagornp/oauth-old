/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.collection;

import com.pamarin.commons.util.ObjectEquals;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @author jitta
 */
@Getter
@Setter
@Builder
@Document(collection = LoginHistory.COLLECTION_NAME)
public class LoginHistory implements Serializable {

    public static final String COLLECTION_NAME = "login_history";

    @Id
    private String id;

    @Field("login_date")
    private LocalDateTime loginDate;

    @Field("logout_date")
    private LocalDateTime logoutDate;

    @Field("session_id")
    private String sessionId;

    @Field("agent_id")
    private String agentId;

    @Field("user_id")
    private String userId;

    @Field("ip_address")
    private String ipAddress;

    public LoginHistory() {
    }

    public LoginHistory(String id, LocalDateTime loginDate, LocalDateTime logoutDate, String sessionId, String agentId, String userId, String ipAddress) {
        this.id = id;
        this.loginDate = loginDate;
        this.logoutDate = logoutDate;
        this.sessionId = sessionId;
        this.agentId = agentId;
        this.userId = userId;
        this.ipAddress = ipAddress;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return ObjectEquals.of(this)
                .equals(obj, (origin, other) -> {
                    return Objects.equals(origin.getId(), other.getId());
                });
    }

}
