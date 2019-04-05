/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.domain;

import com.pamarin.commons.util.ObjectEquals;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/21
 */
@Getter
@Setter
@Entity
@Table(name = OAuth2AllowDomain.TABLE_NAME)
public class OAuth2AllowDomain extends AuditingEntity {

    public static final String TABLE_NAME = "oauth2_allow_domain";

    @Id
    @TableGenerator(
            name = TABLE_NAME,
            table = "sequence",
            pkColumnValue = TABLE_NAME,
            allocationSize = 1,
            initialValue = 0
    )
    @GeneratedValue(
            generator = TABLE_NAME,
            strategy = GenerationType.TABLE
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "domain_name", nullable = false)
    private String domainName;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id", insertable = false, updatable = false)
    private OAuth2Client client;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
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
