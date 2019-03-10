/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security.hashing;

/**
 *
 * @author jitta
 */
public class FixedResultLengthHashing implements Hashing {

    private final Hashing hashing;

    private final int length;

    public FixedResultLengthHashing(Hashing hashing, int length) {
        this.hashing = hashing;
        this.length = length;
        if (length < 32) {
            throw new IllegalArgumentException("length must more than or equals 32.");
        }
    }

    private String subString(String token) {
        return token.substring(0, length);
    }

    @Override
    public String hash(byte[] data) {
        return subString(hashing.hash(data));
    }

    @Override
    public boolean matches(byte[] data, String token) {
        try {
            return hashing.matches(data, subString(token));
        } catch (StringIndexOutOfBoundsException ex) {
            return false;
        }
    }

}
