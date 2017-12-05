/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.exception.AuthenticationException;
import com.pamarin.commons.exception.RSAEncryptionException;
import com.pamarin.commons.security.Base64RSAEncryption;
import com.pamarin.commons.security.DefaultHashBasedToken;
import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.HashBasedToken;
import com.pamarin.commons.security.RSAKeyPairs;
import com.pamarin.commons.security.SHA256CheckSum;
import com.pamarin.oauth2.domain.OAuth2AccessToken;
import com.pamarin.oauth2.exception.InvalidTokenException;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepo;
import com.pamarin.oauth2.service.AccessTokenVerification.Output;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.Month;
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
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/04
 */
public class AccessTokenVerificationTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private AccessTokenVerificationImpl verification;

    @Mock
    private OAuth2AccessTokenRepo accessTokenRepo;

    private HashBasedToken hashBasedToken;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        hashBasedToken = new DefaultHashBasedToken("abcd", new SHA256CheckSum());
        ReflectionTestUtils.setField(
                verification,
                "hashBasedToken",
                hashBasedToken
        );

        when(accessTokenRepo.findById("123456"))
                .thenReturn(null);
        when(accessTokenRepo.findById("999999"))
                .thenReturn(OAuth2AccessToken.builder()
                        .id("999999")
                        .build());
    }

    public String makeToken(String id, LocalDateTime expires) {
        return hashBasedToken.hash(DefaultUserDetails.builder()
                .username(id)
                .password(id)
                .build(),
                expires
        );
    }

    @Test
    public void shouldBeInvalidToken_whenTokenNotFound() {

        exception.expect(InvalidTokenException.class);
        exception.expectMessage("Invalid access token.");

        String input = "123456";
        Output output = verification.verify(makeToken(input, LocalDateTime.now()));

    }

    @Test
    public void shouldBeOk() {
        String input = "999999";
        Output output = verification.verify(makeToken(input, LocalDateTime.now().plusDays(1)));
        String expected = input;
        assertThat(output.getId()).isEqualTo(expected);
    }
}
