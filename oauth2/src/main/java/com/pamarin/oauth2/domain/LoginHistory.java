/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
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
@Document(collection = LoginHistory.TABLE_NAME)
public class LoginHistory implements Serializable {

    public static final String TABLE_NAME = "login_history";

    @Id
    private String id;

    private LocalDateTime loginDate;

    private LocalDateTime logoutDate;

    private String sessionId;

    private String agentId;

    private String userId;

    private String ipAddress;

}
