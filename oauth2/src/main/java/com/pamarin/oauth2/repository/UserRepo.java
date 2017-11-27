/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/18
 */
public interface UserRepo extends JpaRepository<User, String> {

    User findByUsername(String username);

}
