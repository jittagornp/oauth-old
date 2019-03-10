/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security.hashing;

import java.security.MessageDigest;
import static org.apache.commons.lang.ArrayUtils.isEmpty;

/**
 *
 * @author jitta
 */
public abstract class AbstractHashing implements Hashing {

    @Override
    public boolean matches(byte[] data, String token) {
        if (token == null || isEmpty(data)) {
            return false;
        }
        return MessageDigest.isEqual(hash(data).getBytes(), token.getBytes());
    }

}
