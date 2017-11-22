/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.OAuth2AllowDomain;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/21
 */
public interface OAuth2AllowDomainRepo extends JpaRepository<OAuth2AllowDomain, Long> {

    @Query("SELECT a.domainName FROM OAuth2AllowDomain a WHERE a.clientId = ?1")
    List<String> findDomainNameByClientId(String clientId);

}
