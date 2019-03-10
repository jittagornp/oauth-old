/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.domain.OAuth2AccessToken;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
public interface AccessTokenVerification {

    OAuth2AccessToken verify(String accessToken);

}
