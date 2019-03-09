/*
 * Copyright 2017-2019 Pamarin.com
 */

package com.pamarin.oauth2.service;

import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.model.AuthorizationResponse;

/**
 * @author jittagornp <http://jittagornp.me>  
 * create : 2017/09/26
 */
@FunctionalInterface
public interface AuthorizationCodeGenerator {

    AuthorizationResponse generate(AuthorizationRequest authReq);
    
}
