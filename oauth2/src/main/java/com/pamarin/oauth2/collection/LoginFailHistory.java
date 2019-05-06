/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.collection;

import static com.pamarin.commons.util.DateConverterUtils.convert2Timestamp;
import com.pamarin.commons.util.ObjectEquals;
import static java.time.LocalDateTime.now;
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
@Document(collection = LoginFailHistory.COLLECTION_NAME)
public class LoginFailHistory {

    public static final String COLLECTION_NAME = "login_fail_history";

    @Id
    private String id;

    private String username;

    private long creationTime;

    private long expirationTime;

    private String ipAddress;

    public boolean isExpired() {
        long now = convert2Timestamp(now());
        return now >= expirationTime;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return ObjectEquals.of(this)
                .equals(obj, (origin, other) -> Objects.equals(origin.getId(), other.getId()));
    }

}
