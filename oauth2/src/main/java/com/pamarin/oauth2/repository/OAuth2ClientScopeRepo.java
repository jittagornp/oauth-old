/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.OAuth2ClientScope;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/21
 */
public interface OAuth2ClientScopeRepo extends JpaRepository<OAuth2ClientScope, OAuth2ClientScope.PK> {

    @Query("SELECT s.id.scope FROM OAuth2ClientScope s WHERE s.id.clientId = ?1")
    List<String> findScopeByClientId(String clientId);

}
