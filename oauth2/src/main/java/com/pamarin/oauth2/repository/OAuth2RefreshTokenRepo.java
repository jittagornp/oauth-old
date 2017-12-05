/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.oauth2.domain.OAuth2RefreshToken;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
public interface OAuth2RefreshTokenRepo {

    OAuth2RefreshToken save(OAuth2RefreshToken refreshToken);

    OAuth2RefreshToken findById(String id);

}
