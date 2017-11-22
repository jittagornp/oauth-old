/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.security;

import com.pamarin.oauth2.util.ByteUtils;
import java.security.MessageDigest;
import java.util.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * implement following
 * https://medium.com/rubyinside/a-deep-dive-into-csrf-protection-in-rails-19fa0a42c0ef
 *
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/15
 */
public class DefaultAuthenticityToken implements AuthenticityToken {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuthenticityToken.class);

    private final int size;

    public DefaultAuthenticityToken(int size) {
        this.size = size;
    }

    private String base64Encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private String base64Decode(String data) {
        return new String(Base64.getDecoder().decode(data));
    }

    private String xorByteString(String a, String b) {
        return new String(ByteUtils.xor(a.getBytes(), b.getBytes()));
    }

    private String randomRawToken() {
        return RandomStringUtils.randomAlphanumeric(size);
    }

    //https://www.wikiwand.com/en/One-time_pad
    private String randomOneTimePad() {
        return RandomStringUtils.randomAscii(size);
    }

    @Override
    public RandomOutput random() {
        String oneTimePad = randomOneTimePad();
        String rawToken = randomRawToken();
        String xorToken = xorByteString(oneTimePad, rawToken);
        String maskedToken = oneTimePad + xorToken;
        return new RandomOutput(rawToken, base64Encode(maskedToken.getBytes()));
    }

    @Override
    public String decode(String authenticityToken) {
        try {
            String maskedToken = base64Decode(authenticityToken);
            String oneTimePad = maskedToken.substring(0, size);
            String xorToken = maskedToken.substring(size);
            return xorByteString(oneTimePad, xorToken);
        } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
            LOG.warn("Not valid token", ex);
            return null;
        }
    }

    @Override
    public boolean matches(String token, String authenticityToken) {
        return isEquals(token, decode(authenticityToken));
    }

    /**
     * protect Timing Attacks against String Comparison
     * https://thisdata.com/blog/timing-attacks-against-string-comparison/
     *
     * https://security.stackexchange.com/questions/83660/simple-string-comparisons-not-secure-against-timing-attacks/83671#83671
     *
     * @param str1
     * @param str2
     * @return
     */
    private boolean isEquals(String str1, String str2) {
        return MessageDigest.isEqual(str1.getBytes(), str2.getBytes());
    }
}
