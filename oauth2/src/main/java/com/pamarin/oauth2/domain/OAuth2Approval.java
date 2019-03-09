/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.domain;

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
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final PK other = (PK) obj;
            if (!Objects.equals(this.clientId, other.clientId)) {
                return false;
            }
            if (!Objects.equals(this.userId, other.userId)) {
                return false;
            }
            return true;
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OAuth2Approval other = (OAuth2Approval) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
