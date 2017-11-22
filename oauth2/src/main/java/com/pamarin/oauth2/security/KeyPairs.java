/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/13
 */
public interface KeyPairs {

    RSAPrivateKey getRSAPrivateKey();

    RSAPublicKey getRSAPublicKey();

}
