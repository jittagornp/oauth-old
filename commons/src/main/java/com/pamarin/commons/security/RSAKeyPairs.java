/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/13
 */
public interface RSAKeyPairs {

    RSAPrivateKey getPrivateKey();

    RSAPublicKey getPublicKey();

}
