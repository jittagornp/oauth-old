/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.exception.RSAKeyReaderException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/11
 */
@Component
class DefaultRSAPrivateKeyReader implements RSAPrivateKeyReader {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultRSAPrivateKeyReader.class);

    @Override
    public RSAPrivateKey readFromDERFile(InputStream inputStream) {
        try {
            if (inputStream == null) {
                throw new RSAKeyReaderException("Required inputStream.");
            }
            byte[] keyBytes = IOUtils.toByteArray(inputStream);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) kf.generatePrivate(spec);
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
