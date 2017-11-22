/*
 * Copyright 2017 Pamarin.com
 */

package com.pamarin.commons.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt;  
 * create : 2017/11/19
 */
public interface UserSession extends UserDetails {
   
    Long getId();
    
}
