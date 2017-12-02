/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.ClassPathDERFileRSAKeyPairsAdapter;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/13
 */
@Component("autorizationCodeKeyPairs")
public class AutorizationCodeKeyPairs extends ClassPathDERFileRSAKeyPairsAdapter {

    @Override
    protected String getPrivateKeyPath() {
        return "/key/private-key.der";
    }

    @Override
    protected String getPublicKeyPath() {
        return "/key/public-key.der";
    }

}
