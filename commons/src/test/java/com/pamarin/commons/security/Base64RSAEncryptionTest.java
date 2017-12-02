/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.IntegrationTestBase;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/02
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Base64RSAEncryptionTest extends IntegrationTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(Base64RSAEncryptionTest.class);

    @Autowired
    private Base64RSAEncryption rsaEncryption;

    @Autowired
    private RSAKeyPairsStub keyPairs;

    @Test(expected = IllegalArgumentException.class)
    public void shouldBeThrowIllegalArgumentException_whenDataIsNull_onEncrypt() {
        String input = null;
        String output = rsaEncryption.encrypt(input, keyPairs.getPrivateKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldBeThrowIllegalArgumentException_whenDataIsNull_onDecrypt() {
        String input = null;
        String output = rsaEncryption.decrypt(input, keyPairs.getPublicKey());
    }

    @Test
    public void shouldBeOk() {
        String input = "Hello World";
        String encrypted = rsaEncryption.encrypt(input, keyPairs.getPrivateKey());
        LOG.debug("encrypted => {}", encrypted);
        String decrypted = rsaEncryption.decrypt(encrypted, keyPairs.getPublicKey());
        LOG.debug("decrypted => {}", decrypted);
        assertThat(input).isEqualTo(decrypted);
    }

}
