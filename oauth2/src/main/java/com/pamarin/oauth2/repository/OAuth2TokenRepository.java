/*
 * Copyright 2017-2019 Pamarin.com
 */

package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.model.OAuth2Token;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt;  
 * create : 2017/12/03
 * @param <T>
 */
public interface OAuth2TokenRepository <T extends OAuth2Token> {

    T save(T token);
    
    T findByTokenId(String tokenId);
    
    void deleteByTokenId(String tokenId);
}
