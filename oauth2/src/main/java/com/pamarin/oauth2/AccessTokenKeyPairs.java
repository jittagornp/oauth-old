/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.ClassPathDERFileRSAKeyPairsAdapter;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/13
 */
@Component("accessTokenKeyPairs")
public class AccessTokenKeyPairs extends ClassPathDERFileRSAKeyPairsAdapter {

    @Override
    protected String getPrivateKeyPath() {
        return "/key/private-key.der";
    }

    @Override
    protected String getPublicKeyPath() {
        return "/key/public-key.der";
    }

}
