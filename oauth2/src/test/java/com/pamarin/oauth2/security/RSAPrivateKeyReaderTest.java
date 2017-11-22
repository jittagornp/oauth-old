/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.security;

import com.pamarin.oauth2.AppStarter;
import com.pamarin.oauth2.IntegrationTestBase;
import com.pamarin.oauth2.exception.RSAKeyReaderException;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/11
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppStarter.class)
public class RSAPrivateKeyReaderTest extends IntegrationTestBase {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private RSAPrivateKeyReader keyReader;

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
