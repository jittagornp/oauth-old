/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.exception.AESEncryptionException;
import java.io.UnsupportedEncodingException;
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
//@Component
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

    private String getKeyFixed(String key) {
        if (key.length() > keyLength) {
            key = key.substring(0, keyLength);
        }
        return StringUtils.rightPad(key, keyLength);
    }

    private SecretKeySpec makeSecretKeySpec(String key) throws UnsupportedEncodingException {
        return new SecretKeySpec(getKeyFixed(key).getBytes("UTF-8"), ALGORITHM);
    }

    @Override
    public byte[] encrypt(byte[] data, String key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, makeSecretKeySpec(key));
            return cipher.doFinal(data);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new AESEncryptionException(ex);
        }
    }

    @Override
    public byte[] decrypt(byte[] data, String key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, makeSecretKeySpec(key));
            return cipher.doFinal(data);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new AESEncryptionException(ex);
        }
    }

}
