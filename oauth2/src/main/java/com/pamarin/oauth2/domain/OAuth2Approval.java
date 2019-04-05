/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.domain;

import com.pamarin.commons.util.ObjectEquals;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/19
 */
@Getter
@Setter
@Entity
@Table(name = OAuth2Approval.TABLE_NAME)
public class OAuth2Approval extends AuditingEntity {

    public static final String TABLE_NAME = "oauth2_approval";

    @Getter
    @Setter
    @Embeddable
    public static class PK implements Serializable {

        @Column(name = "user_id")
        private String userId;

        @Column(name = "client_id")
        private String clientId;

        public PK() {

        }

        public PK(String userId, String clientId) {
            this.userId = userId;
            this.clientId = clientId;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + Objects.hashCode(this.userId);
            hash = 59 * hash + Objects.hashCode(this.clientId);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            return ObjectEquals.of(this)
                    .equals(obj, (origin, other) -> {
                        if (!Objects.equals(origin.getClientId(), other.getClientId())) {
                            return false;
                        }
                        return Objects.equals(origin.getUserId(), other.getUserId());
                    });
        }

    }

    @EmbeddedId
    private PK id;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
