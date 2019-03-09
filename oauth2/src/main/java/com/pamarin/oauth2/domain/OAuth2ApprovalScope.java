/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/20
 */
@Getter
@Setter
@Entity
@Table(name = OAuth2ApprovalScope.TABLE_NAME)
public class OAuth2ApprovalScope extends AuditingEntity {

    public static final String TABLE_NAME = "oauth2_approval_scope";

    @Id
    @TableGenerator(name = TABLE_NAME,
            table = "sequence",
            pkColumnValue = TABLE_NAME,
            allocationSize = 1,
            initialValue = 0
    )
    @GeneratedValue(generator = TABLE_NAME, strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "scope", nullable = false)
    private String scope;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
        @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
        ,@JoinColumn(name = "client_id", referencedColumnName = "client_id", insertable = false, updatable = false)
    })
    private OAuth2Approval approval;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final OAuth2ApprovalScope other = (OAuth2ApprovalScope) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
