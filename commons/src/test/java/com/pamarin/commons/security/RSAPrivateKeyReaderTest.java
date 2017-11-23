/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.exception.RSAKeyReaderException;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/11
 */
public class RSAPrivateKeyReaderTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private RSAPrivateKeyReader keyReader;
    
    @Before
    public void before(){
        keyReader = new DefaultRSAPrivateKeyReader();
    }

    @Test
    public void shouldBeThrowRSAKeyReaderException_whenInputIsNull() {
        exception.expect(RSAKeyReaderException.class);
        exception.expectMessage("Required inputStream.");

        InputStream input = null;
        RSAPrivateKey output = keyReader.readFromDERFile(input);
        assertThat(output).isNotNull();
    }

    @Test
    public void shouldBeNotNull() {
        InputStream input = getClass().getResourceAsStream("/key/private-key.der");
        RSAPrivateKey output = keyReader.readFromDERFile(input);
        assertThat(output).isNotNull();
    }

}
