/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.OAuth2Scope;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/21
 */
public interface OAuth2ScopeRepository extends JpaRepository<OAuth2Scope, String> {

}
