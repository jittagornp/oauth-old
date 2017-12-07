/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static org.apache.commons.lang.ArrayUtils.isEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Hex;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/29
 */
public class SHA384Hashing implements Hashing {

    private static final Logger LOG = LoggerFactory.getLogger(SHA384Hashing.class);

    @Override
    public String hash(byte[] data) {
        try {
            if (isEmpty(data)) {
                return null;
            }

            MessageDigest digest = MessageDigest.getInstance("SHA-384");
            return new String(Hex.encode(digest.digest(data)));
        } catch (NoSuchAlgorithmException ex) {
            LOG.warn(null, ex);
            return null;
        }
    }

    @Override
    public boolean matches(byte[] data, String token) {
        if (token == null || isEmpty(data)) {
            return false;
        }
        return MessageDigest.isEqual(hash(data).getBytes(), token.getBytes());
    }
}
