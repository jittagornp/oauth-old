/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/13
 */
public abstract class ClassPathDERFileKeyPairsAdapter implements KeyPairs {

    @Autowired
    private RSAPrivateKeyReader privateKeyReader;

    @Autowired
    private RSAPublicKeyReader publicKeyReader;

    private RSAPrivateKey privateKey;

    private RSAPublicKey publicKey;

    protected abstract String getPrivateKey();

    protected abstract String getPublicKey();

    @Override
    public RSAPrivateKey getRSAPrivateKey() {
        if (privateKey == null) {
            privateKey = privateKeyReader.readFromDERFile(getClass().getResourceAsStream(getPrivateKey()));
        }
        return privateKey;
    }

    @Override
    public RSAPublicKey getRSAPublicKey() {
        if (publicKey == null) {
            publicKey = publicKeyReader.readFromDERFile(getClass().getResourceAsStream(getPublicKey()));
        }
        return publicKey;
    }

}
