/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.exception.RSAEncryptionException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/02
 */
@Component
public class DefaultRSAEncryption implements RSAEncryption {

    @Override
    public byte[] encrypt(byte[] data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new RSAEncryptionException(ex);
        }
    }

    @Override
    public byte[] decrypt(byte[] data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new RSAEncryptionException(ex);
        }
    }

}
