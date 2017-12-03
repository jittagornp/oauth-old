/*
 * Copyright 2017 Pamarin.com
 */

package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.IOAuth2Token;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt;  
 * create : 2017/12/03
 * @param <TOKEN>
 */
public interface OAuth2TokenRepo <TOKEN extends IOAuth2Token> {

    TOKEN save(TOKEN token);
    
    TOKEN findById(String id);
    
}
