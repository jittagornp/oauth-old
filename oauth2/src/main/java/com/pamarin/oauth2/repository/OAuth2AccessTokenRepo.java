/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.OAuth2AccessToken;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
public interface OAuth2AccessTokenRepo {

    OAuth2AccessToken save(OAuth2AccessToken accessToken);

    OAuth2AccessToken findById(String id);

}
