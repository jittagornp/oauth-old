/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.security.ClassPathDERFileKeyPairsAdapter;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/13
 */
@Component("autorizationCodeKeyPairs")
public class AutorizationCodeKeyPairs extends ClassPathDERFileKeyPairsAdapter {

    @Override
    protected String getPrivateKey() {
        return "/key/private-key.der";
    }

    @Override
    protected String getPublicKey() {
        return "/key/public-key.der";
    }

}
