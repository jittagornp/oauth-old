/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.model.AuthorizationRequest;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/25
 */
public interface AuthorizationService {

    String authorize(AuthorizationRequest req);
    
    String approved(AuthorizationRequest req);
    
    String notApprove(AuthorizationRequest req);

}
