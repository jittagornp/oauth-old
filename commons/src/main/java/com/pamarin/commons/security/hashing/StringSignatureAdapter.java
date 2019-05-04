/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security.hashing;

import com.pamarin.commons.exception.InvalidSignatureException;
import org.springframework.util.Assert;

/**
 *
 * @author jitta
 */
public abstract class StringSignatureAdapter implements StringSignature {

    protected abstract Hashing getHashing();

    @Override
    public String sign(String string) {
        Assert.hasText(string, "require string.");
        return getHashing().hash(string.getBytes());
    }

    @Override
    public void verify(String string, String signature) {
        Assert.hasText(string, "require string.");
        Assert.hasText(string, "require signature.");
        if (!getHashing().matches(string.getBytes(), signature)) {
            throw new InvalidSignatureException("Invalid signature \"" + signature + "\".");
        }
    }
}
