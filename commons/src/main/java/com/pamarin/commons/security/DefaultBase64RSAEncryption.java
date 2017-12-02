/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/02
 */
@Component
public class DefaultBase64RSAEncryption implements Base64RSAEncryption {

    @Autowired
    private RSAEncryption rsaEncryption;

    @Override
    public String encrypt(String data, RSAPrivateKey privateKey) {
        if (data == null) {
            throw new IllegalArgumentException("Requires data.");
        }
        return Base64.getEncoder().encodeToString(rsaEncryption.encrypt(data.getBytes(), privateKey));
    }

    @Override
    public String decrypt(String data, RSAPublicKey publicKey) {
        if (data == null) {
            throw new IllegalArgumentException("Requires data.");
        }
        return new String(rsaEncryption.decrypt(Base64.getDecoder().decode(data), publicKey));
    }

}
