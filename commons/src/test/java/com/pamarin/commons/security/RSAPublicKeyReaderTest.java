/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.exception.RSAKeyReaderException;
import java.io.InputStream;
import java.security.interfaces.RSAPublicKey;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/11
 */
public class RSAPublicKeyReaderTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private RSAPublicKeyReader keyReader;

    @Before
    public void before() {
        keyReader = new DefaultRSAPublicKeyReader();
    }

    @Test
    public void shouldBeThrowRSAKeyReaderException_whenInputIsNull() {
        exception.expect(RSAKeyReaderException.class);
        exception.expectMessage("Required inputStream.");

        InputStream input = null;
        RSAPublicKey output = keyReader.readFromDERFile(input);
        assertThat(output).isNotNull();
    }

    @Test
    public void shouldBeNotNull() {
        InputStream input = getClass().getResourceAsStream("/key/public-key.der");
        RSAPublicKey output = keyReader.readFromDERFile(input);
        assertThat(output).isNotNull();
    }

}
