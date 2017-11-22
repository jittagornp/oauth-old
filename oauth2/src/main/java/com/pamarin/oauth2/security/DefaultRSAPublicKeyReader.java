/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.security;

import com.pamarin.oauth2.exception.RSAKeyReaderException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/11
 */
@Component
class DefaultRSAPublicKeyReader implements RSAPublicKeyReader {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultRSAPublicKeyReader.class);

    @Override
    public RSAPublicKey readFromDERFile(InputStream inputStream) {
        try {
            if (inputStream == null) {
                throw new RSAKeyReaderException("Required inputStream.");
            }
            byte[] keyBytes = IOUtils.toByteArray(inputStream);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            LOG.warn(null, ex);
            throw new RSAKeyReaderException(ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    LOG.warn(null, ex);
                }
            }
        }
    }

}
