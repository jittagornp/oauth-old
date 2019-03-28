/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.collection.OAuth2RefreshToken;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/05
 */
public interface RefreshTokenVerification {

    OAuth2RefreshToken verify(String token);

}
