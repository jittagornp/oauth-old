/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security.hashing;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import static org.apache.commons.lang.ArrayUtils.isEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Hex;

/**
 * HMAC https://www.wikiwand.com/en/Hash-based_message_authentication_code
 *
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/07
 */
public class HmacHashing extends AbstractHashing {

    private static final Logger LOG = LoggerFactory.getLogger(HmacHashing.class);

    private final String hmacAlgorithm;

    private final String privateKey;

    public HmacHashing(String hmacAlgorithm, String privateKey) {
        this.hmacAlgorithm = hmacAlgorithm;
        this.privateKey = privateKey;
    }

    @Override
    public String hash(byte[] data) {
        try {
            if (isEmpty(data)) {
                return null;
            }

            SecretKeySpec signingKey = new SecretKeySpec(this.privateKey.getBytes(), hmacAlgorithm);
            Mac mac = Mac.getInstance(hmacAlgorithm);
            mac.init(signingKey);
            return new String(Hex.encode(mac.doFinal(data)));
        } catch (InvalidKeyException | NoSuchAlgorithmException ex) {
            LOG.warn(null, ex);
            return null;
        }
    }

}
