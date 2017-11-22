/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.model.AuthorizationRequest;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/12
 */
@FunctionalInterface
public interface AuthorizationRequestVerification {

    void verify(AuthorizationRequest authReq);

}
