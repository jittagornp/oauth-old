/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security.hashing;

/**
 *
 * @author jitta
 */
public class ShortHashing extends AbstractHashing {

    private final Hashing hashing;

    private final int length;

    public ShortHashing(Hashing hashing, int length) {
        this.hashing = hashing;
        this.length = length;
        if (length < 8) {
            throw new IllegalArgumentException("length must more than or equals 8.");
        }
    }

    private String subString(String token) {
        if (token == null) {
            return null;
        }
        if (token.length() <= length) {
            return token;
        }
        return token.substring(0, length);
    }

    @Override
    public String hash(byte[] data) {
        return subString(hashing.hash(data));
    }

}
