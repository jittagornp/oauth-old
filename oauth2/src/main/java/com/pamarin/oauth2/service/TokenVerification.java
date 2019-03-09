/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.model.TokenBase;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@FunctionalInterface
public interface TokenVerification {

    TokenBase verify(String token);

}
