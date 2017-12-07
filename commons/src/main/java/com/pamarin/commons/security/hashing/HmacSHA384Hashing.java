/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security.hashing;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Hex;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/07
 */
public class HmacSHA384Hashing implements Hashing {

    private static final Logger LOG = LoggerFactory.getLogger(HmacSHA384Hashing.class);

    private static final String HMAC_SHA384 = "HmacSHA384";

    private final String privateKey;

    public HmacSHA384Hashing(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String hash(byte[] data) {
        if (isEmpty(data)) {
            return null;
        }

        try {
            SecretKeySpec signingKey = new SecretKeySpec(this.privateKey.getBytes(), HMAC_SHA384);
            Mac mac = Mac.getInstance(HMAC_SHA384);
            mac.init(signingKey);
            return new String(Hex.encode(mac.doFinal(data)));
        } catch (InvalidKeyException | NoSuchAlgorithmException ex) {
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
