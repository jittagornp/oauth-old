/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.exception.AuthenticationException;
import com.pamarin.commons.exception.RSAEncryptionException;
import com.pamarin.commons.security.Base64RSAEncryption;
import com.pamarin.commons.security.RSAKeyPairs;
import com.pamarin.oauth2.domain.OAuth2AccessToken;
import com.pamarin.oauth2.exception.InvalidTokenException;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepo;
import com.pamarin.oauth2.service.AccessTokenVerification.Output;
import java.security.interfaces.RSAPublicKey;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/04
 */
public class AccessTokenVerificationTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private AccessTokenVerificationImpl verification;

    @Mock
    private Base64RSAEncryption base64RSAEncryption;

    @Mock
    private RSAKeyPairs keyPairs;

    @Mock
    private OAuth2AccessTokenRepo accessTokenRepo;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldBeErrorInvalidToDecryptAccessToken() {

        when(base64RSAEncryption.decrypt(any(String.class), any(RSAPublicKey.class)))
                .thenThrow(RSAEncryptionException.class);

        exception.expect(InvalidTokenException.class);
        exception.expectMessage("Invalid to decrypt access token.");

        String input = "xyz";
        Output output = verification.verify(input);

    }

    @Test
    public void shouldBeErrorAccessTokenNotFound() {

        when(accessTokenRepo.findById("xyz")).thenReturn(null);

        exception.expect(AuthenticationException.class);
        exception.expectMessage("Access token not found.");

        String input = "xyz";
        Output output = verification.verify(input);
    }

    @Test
    public void shouldBeErrorInvalidAccessTokenId() {

        when(base64RSAEncryption.decrypt(any(String.class), any(RSAPublicKey.class)))
                .thenReturn("abc");

        when(accessTokenRepo.findById("abc")).thenReturn(
                OAuth2AccessToken.builder()
                        .id("abc")
                        .build()
        );

        exception.expect(InvalidTokenException.class);
        exception.expectMessage("Invalid access token id.");

        String input = "xyz";
        Output output = verification.verify(input);

    }

    @Test
    public void shouldBeOk() {

        when(base64RSAEncryption.decrypt(any(String.class), any(RSAPublicKey.class)))
                .thenReturn("abc");

        when(accessTokenRepo.findById("abc")).thenReturn(
                OAuth2AccessToken.builder()
                        .id("abc:123456")
                        .build()
        );

        String input = "xyz";
        Output output = verification.verify(input);
        String expected = "123456";
        assertThat(output.getId()).isEqualTo(expected);
    }

}
