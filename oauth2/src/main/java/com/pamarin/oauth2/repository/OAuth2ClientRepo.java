/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.OAuth2Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/21
 */
public interface OAuth2ClientRepo extends JpaRepository<OAuth2Client, String> {

    @Query("SELECT c.secret FROM OAuth2Client c WHERE c.id = ?1")
    String findSecretById(String id);

}
