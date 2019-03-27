/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private LocalDateTime loginDate;

    private LocalDateTime logoutDate;

    private String sessionId;

    private String agentId;

    private String userId;

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

}
