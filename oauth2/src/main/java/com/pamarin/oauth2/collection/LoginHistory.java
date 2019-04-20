/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.collection;

import com.pamarin.commons.util.ObjectEquals;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return ObjectEquals.of(this)
                .equals(obj, (origin, other) -> Objects.equals(origin.getId(), other.getId()));
    }

}
