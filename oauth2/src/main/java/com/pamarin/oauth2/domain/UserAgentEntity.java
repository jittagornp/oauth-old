/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jitta
 */
@Getter
@Setter
@Entity
@Table(name = UserAgentEntity.TABLE_NAME)
public class UserAgentEntity implements Serializable {

    public static final String TABLE_NAME = "user_agent";

    @Id
    private String id;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "device_type_key")
    private String deviceTypeKey; //COMPUTER, MOBILE, TABLET, WEARABLE

    @Column(name = "device_type_name")
    private String deviceTypeName; //Computer

    @Column(name = "browser_type_key")
    private String browserTypeKey; //WEB_BROWSER, MOBILE_BROWSER

    @Column(name = "browser_type_name")
    private String browserTypeName; //Browser

    @Column(name = "browser_key")
    private String browserKey; //CHROME

    @Column(name = "browser_name")
    private String browserName; //Chrome

    @Column(name = "browser_group_key")
    private String browserGroupKey; //CHROME

    @Column(name = "browser_group_name")
    private String browserGroupName; //Chrome

    @Column(name = "browser_rendering_engine")
    private String browserRenderingEngine; //WEBKIT

    @Column(name = "browser_manufacturer_key")
    private String browserManufacturerKey; //GOOGLE

    @Column(name = "browser_manufacturer_name")
    private String browserManufacturerName; //Google Inc.

    @Column(name = "os_manufacturer_key")
    private String osManufacturerKey; //MICROSOFT, GOOGLE, APPLE

    @Column(name = "os_manufacturer_name")
    private String osManufacturerName; //Microsoft Corporation

    @Column(name = "os_group_key")
    private String osGroupKey; //WINDOWS, LINUX, MAC_OS

    @Column(name = "os_group_name")
    private String osGroupName; //Windows

    @Column(name = "os_key")
    private String osKey; //WINDOWS_10

    @Column(name = "os_name")
    private String osName; //Windows 10

    @Column(name = "agent_version")
    private String agentVersion; //52.0.2743.116

    @Column(name = "agent_major_version")
    private String agentMajorVersion; //52

    @Column(name = "agent_minor_version")
    private String agentMinorVersion; //0

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.id);
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
        final UserAgentEntity other = (UserAgentEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
