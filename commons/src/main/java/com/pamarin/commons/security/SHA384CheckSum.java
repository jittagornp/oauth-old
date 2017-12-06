/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/29
 */
@Component("sha384CheckSum")
public class SHA384CheckSum implements CheckSum {

    private static final Logger LOG = LoggerFactory.getLogger(SHA384CheckSum.class);

    @Override
    public String hash(byte[] data) {
        if (isEmpty(data)) {
            return null;
        }

        try {
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

    private boolean isEmpty(byte[] arr) {
        return arr == null || arr.length < 1;
    }
}
