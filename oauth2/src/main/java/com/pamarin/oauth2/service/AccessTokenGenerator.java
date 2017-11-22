/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.model.AccessTokenResponse;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.model.CodeAccessTokenRequest;
import com.pamarin.oauth2.model.RefreshAccessTokenRequest;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/26
 */
public interface AccessTokenGenerator {

    AccessTokenResponse generate(AuthorizationRequest request);

    AccessTokenResponse generate(CodeAccessTokenRequest request);
    
    AccessTokenResponse generate(RefreshAccessTokenRequest request);

}
