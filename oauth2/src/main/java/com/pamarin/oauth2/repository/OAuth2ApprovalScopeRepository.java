/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.OAuth2ApprovalScope;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/20
 */
public interface OAuth2ApprovalScopeRepository extends JpaRepository<OAuth2ApprovalScope, Long> {

    @Query("SELECT s.scope FROM OAuth2ApprovalScope s WHERE s.userId  = ?1 AND s.clientId = ?2")
    List<String> findScopeByUserIdAndClientId(String userId, String clientId);

}
