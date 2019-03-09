/*
 * Copyright 2017-2019 Pamarin.com
 */

package com.pamarin.commons.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt;  
 * create : 2017/12/02
 */
public interface Base64RSAEncryption {

    String encrypt(String data, RSAPrivateKey privateKey);

    String decrypt(String data, RSAPublicKey publicKey);
    
}
