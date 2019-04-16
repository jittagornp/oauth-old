/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.util.Base64Utils;
import java.security.SecureRandom;

/**
 *
 * @author jitta
 */
public class DefaultSecurityRandom implements SecurityRandom {

    private final SecureRandom secureRandom = new SecureRandom();

    private final int byteSize;

    public DefaultSecurityRandom(int byteSize) {
        this.byteSize = byteSize;
    }

    @Override
    public String random() {
        byte[] bytes = new byte[byteSize];
        secureRandom.nextBytes(bytes);
        return Base64Utils.encode(bytes);
    }

}
