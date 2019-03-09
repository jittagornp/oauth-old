/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import java.util.Base64;

/**
 *
 * @author jitta
 */
public class DefaultBase64AESEncryption implements Base64AESEncryption {

    private final AESEncryption aesEncryption;

    public DefaultBase64AESEncryption(AESEncryption aesEncryption) {
        this.aesEncryption = aesEncryption;
    }

    @Override
    public String encrypt(String data, String secretKey) {
        if (data == null) {
            throw new IllegalArgumentException("Requires data.");
        }
        return Base64.getEncoder().encodeToString(aesEncryption.encrypt(data.getBytes(), secretKey));
    }

    @Override
    public String decrypt(String data, String secretKey) {
        if (data == null) {
            throw new IllegalArgumentException("Requires data.");
        }
        return new String(aesEncryption.decrypt(Base64.getDecoder().decode(data), secretKey));
    }

}
