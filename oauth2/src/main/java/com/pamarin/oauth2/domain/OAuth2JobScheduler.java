/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.domain;

import com.pamarin.commons.util.ObjectEquals;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author jitta
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = OAuth2JobScheduler.TABLE_NAME)
public class OAuth2JobScheduler implements Serializable {

    public static final String TABLE_NAME = "oauth2_job_scheduler";

    @Id
    private Long id;

    @Column(name = "job_id")
    private String jobId;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    @Column(name = "server_ip")
    private String serverIp;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return ObjectEquals.of(this)
                .equals(obj, (origin, other) -> Objects.equals(origin.getId(), other.getId()));
    }

}
