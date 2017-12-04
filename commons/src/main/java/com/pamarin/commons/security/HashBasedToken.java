/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import java.time.LocalDateTime;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/04
 */
public interface HashBasedToken {

    String hash(UserDetails userDetails, LocalDateTime expires);

    boolean matches(String token, UserDetailsService service);
}
