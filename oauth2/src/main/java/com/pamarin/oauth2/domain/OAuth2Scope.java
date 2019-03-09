/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/21
 */
@Getter
@Setter
@Entity
@Table(name = OAuth2Scope.TABLE_NAME)
public class OAuth2Scope extends AuditingEntity {

    public static final String TABLE_NAME = "oauth2_scope";

    @Id
    private String id;

    @Column(nullable = false)
    private String description;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final OAuth2Scope other = (OAuth2Scope) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
