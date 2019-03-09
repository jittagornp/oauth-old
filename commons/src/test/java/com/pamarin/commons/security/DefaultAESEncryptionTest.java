/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 *
 * @author jitta
 */
public class DefaultAESEncryptionTest {

    private final AESEncryption encryption = DefaultAESEncryption.withKeyLength16();

    private final String key = "Ip0Wf3IMGXiuvLqJt3p1zkRoscALC2E0";

    @Test
    public void encryptHelloWorld() throws UnsupportedEncodingException {
        String input = "Hello World";
        byte[] encrypted = encryption.encrypt(input.getBytes(), key);
        byte[] decrypted = encryption.decrypt(encrypted, key);
        assertThat(new String(decrypted)).isEqualTo(input);
    }

    @Test
    public void encryptJittagornp() throws UnsupportedEncodingException {
        String input = "Jittagornp";
        byte[] encrypted = encryption.encrypt(input.getBytes(), key);
        byte[] decrypted = encryption.decrypt(encrypted, key);
        assertThat(new String(decrypted)).isEqualTo(input);
    }
    
    @Test
    public void encryptUUID() throws UnsupportedEncodingException {
        String input = UUID.randomUUID().toString();
        byte[] encrypted = encryption.encrypt(input.getBytes(), key);
        byte[] decrypted = encryption.decrypt(encrypted, key);
        assertThat(new String(decrypted)).isEqualTo(input);
    }
}
