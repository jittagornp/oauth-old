/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/13
 */
public abstract class ClassPathDERFileRSAKeyPairsAdapter implements RSAKeyPairs {

    @Autowired
    private RSAPrivateKeyReader privateKeyReader;

    @Autowired
    private RSAPublicKeyReader publicKeyReader;

    private RSAPrivateKey privateKey;

    private RSAPublicKey publicKey;

    protected abstract String getPrivateKeyPath();

    protected abstract String getPublicKeyPath();

    @Override
    public RSAPrivateKey getPrivateKey() {
        if (privateKey == null) {
            privateKey = privateKeyReader.readFromDERFile(getClass().getResourceAsStream(getPrivateKeyPath()));
        }
        return privateKey;
    }

    @Override
    public RSAPublicKey getPublicKey() {
        if (publicKey == null) {
            publicKey = publicKeyReader.readFromDERFile(getClass().getResourceAsStream(getPublicKeyPath()));
        }
        return publicKey;
    }

}
