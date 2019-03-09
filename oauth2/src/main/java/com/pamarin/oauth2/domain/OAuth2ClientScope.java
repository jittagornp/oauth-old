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
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/21
 */
@Getter
@Setter
@Entity
@Table(name = OAuth2ClientScope.TABLE_NAME)
public class OAuth2ClientScope extends AuditingEntity {

    public static final String TABLE_NAME = "oauth2_client_scope";

    @Getter
    @Setter
    @Embeddable
    public static class PK implements Serializable {

        @Column(name = "client_id")
        private String clientId;

        private String scope;

        public PK() {

        }

        public PK(String clientId, String scope) {
            this.clientId = clientId;
            this.scope = scope;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + Objects.hashCode(this.clientId);
            hash = 79 * hash + Objects.hashCode(this.scope);
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
            if (!Objects.equals(this.scope, other.scope)) {
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
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final OAuth2ClientScope other = (OAuth2ClientScope) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
