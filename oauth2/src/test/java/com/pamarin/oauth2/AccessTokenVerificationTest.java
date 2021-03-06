/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.DefaultHashBasedToken;
import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.HashBasedToken;
import com.pamarin.commons.security.hashing.SHA384Hashing;
import com.pamarin.oauth2.collection.OAuth2AccessToken;
import com.pamarin.oauth2.exception.InvalidTokenException;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepository;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/04
 */
public class AccessTokenVerificationTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private AccessTokenVerificationImpl verification;

    @Mock
    private OAuth2AccessTokenRepository accessTokenRepository;

    private HashBasedToken hashBasedToken;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        hashBasedToken = new DefaultHashBasedToken("abcd", new SHA384Hashing());
        ReflectionTestUtils.setField(
                verification,
                "hashBasedToken",
                hashBasedToken
        );

        when(accessTokenRepository.findByTokenId("123456"))
                .thenReturn(null);
        when(accessTokenRepository.findByTokenId("999999"))
                .thenReturn(OAuth2AccessToken.builder()
                        .tokenId("999999")
                        .secretKey("999999")
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
        OAuth2AccessToken output = verification.verify(makeToken(input, LocalDateTime.now()));

    }

    @Test
    public void shouldBeOk() {
        String input = "999999";
        OAuth2AccessToken output = verification.verify(makeToken(input, LocalDateTime.now().plusDays(1)));
        String expected = input;
        assertThat(output.getTokenId()).isEqualTo(expected);
    }
}
