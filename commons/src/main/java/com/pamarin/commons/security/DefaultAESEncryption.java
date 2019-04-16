/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.exception.AESEncryptionException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author jitta
 */
public class DefaultAESEncryption implements AESEncryption {

    private static final String ALGORITHM = "AES";

    private final int keyLength;

    private DefaultAESEncryption(int keyLength) {
        this.keyLength = keyLength;
    }

    public static DefaultAESEncryption withKeyLength16() {
        return new DefaultAESEncryption(16);
    }

    public static DefaultAESEncryption withKeyLength24() {
        return new DefaultAESEncryption(24);
    }

    public static DefaultAESEncryption withKeyLength32() {
        return new DefaultAESEncryption(32);
    }

    private String getFixedKey(String secretKey) {
        if (secretKey.length() > keyLength) {
            secretKey = secretKey.substring(0, keyLength);
        }
        return StringUtils.rightPad(secretKey, keyLength);
    }

    private SecretKeySpec makeSecretKeySpec(String secretKey) {
        return new SecretKeySpec(getFixedKey(secretKey).getBytes(), ALGORITHM);
    }

    @Override
    public byte[] encrypt(byte[] data, String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, makeSecretKeySpec(secretKey));
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new AESEncryptionException(ex);
        }
    }

    @Override
    public byte[] decrypt(byte[] data, String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, makeSecretKeySpec(secretKey));
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new AESEncryptionException(ex);
        }
    }

}
