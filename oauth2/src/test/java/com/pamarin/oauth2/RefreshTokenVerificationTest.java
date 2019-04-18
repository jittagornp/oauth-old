/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.HashBasedToken;
import com.pamarin.oauth2.collection.OAuth2RefreshToken;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.service.RefreshTokenVerification;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepository;

/**
 *
 * @author jitta
 */
public class RefreshTokenVerificationTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private RefreshTokenVerificationImpl.UserDetailsServiceImpl userDetailsServiceImpl;

    private OAuth2RefreshTokenRepository refreshTokenRepository;

    private OAuth2RefreshToken output;

    private HashBasedToken hashBasedToken;

    private RefreshTokenVerification refreshTokenVerification;

    @Before
    public void before() {
        refreshTokenRepository = mock(OAuth2RefreshTokenRepository.class);
        output = OAuth2RefreshToken.builder().build();
        userDetailsServiceImpl = new RefreshTokenVerificationImpl.UserDetailsServiceImpl(refreshTokenRepository, output);
        hashBasedToken = mock(HashBasedToken.class);

        refreshTokenVerification = new RefreshTokenVerificationImpl(refreshTokenRepository, hashBasedToken);
    }

    @Test
    public void shouldBeThrowUsernameNotFoundException_whenAccessTokenIsNull() {

        exception.expect(UsernameNotFoundException.class);
        exception.expectMessage("Not found refresh token");

        when(refreshTokenRepository.findByTokenId(any(String.class))).thenReturn(null);

        String input = "test";
        userDetailsServiceImpl.loadUserByUsername(input);
    }

    @Test
    public void shouldBeOkForUserDetailsService() {
        OAuth2RefreshToken refreshToken = OAuth2RefreshToken.builder().build();
        refreshToken.setUserId("jittagornp");
        refreshToken.setSecretKey("xyz");
        when(refreshTokenRepository.findByTokenId(any(String.class))).thenReturn(refreshToken);

        String input = "test";
        userDetailsServiceImpl.loadUserByUsername(input);
        assertThat(output.getUserId()).isEqualTo(refreshToken.getUserId());
        assertThat(output.getSecretKey()).isNull();
    }

    @Test
    public void shouldBeThrowUnauthorizedClientException_whenTokenNotMatches() {

        exception.expect(UnauthorizedClientException.class);
        exception.expectMessage("Invalid refresh token");

        String input = "test";

        when(hashBasedToken.matches(input, userDetailsServiceImpl)).thenReturn(false);

        refreshTokenVerification.verify(input);
    }

    @Test
    public void shouldBeOk() {

        when(hashBasedToken.matches(any(String.class), any(UserDetailsService.class))).thenReturn(true);

        String input = "test";
        refreshTokenVerification.verify(input);
    }

}
