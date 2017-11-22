/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.exception.RequireApprovalException;
import com.pamarin.oauth2.model.AccessTokenResponse;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.model.AuthorizationResponse;
import com.pamarin.commons.security.UserSessionStub;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import com.pamarin.oauth2.validator.ResponseType;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/25
 */
public class AuthorizationService_authorizeWasLoginTest {

    @InjectMocks
    private DefaultAuthorizationService authorizationService;

    @Mock
    private ResponseType.Validator responseTypeValidator;

    @Mock
    private ScopeVerification scopeVerification;

    @Mock
    private ClientVerification clientVerification;

    @Mock
    private LoginSession loginSession;

    @Mock
    private AuthorizationCodeGenerator authorizationCodeGenerator;

    @Mock
    private AccessTokenGenerator accessTokenGenerator;

    @Mock
    private ApprovalService approvalService;

    @Mock
    private AuthorizationRequestVerification requestVerification;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(loginSession.wasCreated()).thenReturn(true);
        when(loginSession.getUserSession()).thenReturn(UserSessionStub.get());
        when(responseTypeValidator.isValid(any(String.class))).thenReturn(true);
        when(approvalService.wasApprovedByUserIdAndClientId(any(Long.class), any(String.class)))
                .thenReturn(true);
    }

    @Test(expected = RequireApprovalException.class)
    public void shouldBeThrowRequireApprovalException_whenNeverApprove() {
        when(approvalService.wasApprovedByUserIdAndClientId(any(Long.class), any(String.class)))
                .thenReturn(false);

        AuthorizationRequest input = AuthorizationRequest.builder()
                .clientId("1234")
                .redirectUri("https://pamarin.com/callback")
                .responseType("code")
                .scope("read")
                .state("XYZ")
                .build();

        String output = authorizationService.authorize(input);
    }

    @Test
    public void shouldBeReturnCode_whenResponseTypeIsCode() {
        when(authorizationCodeGenerator.generate(any(AuthorizationRequest.class)))
                .thenReturn(AuthorizationResponse.builder()
                        .code("ABCD")
                        .build());

        AuthorizationRequest input = AuthorizationRequest.builder()
                .clientId("1234")
                .redirectUri("https://pamarin.com/callback")
                .responseType("code")
                .scope("read")
                .build();

        String output = authorizationService.authorize(input);
        String expected = "https://pamarin.com/callback?code=ABCD";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeReturnCode_whenResponseTypeIsCodeAndHasQuerystring() {
        when(authorizationCodeGenerator.generate(any(AuthorizationRequest.class)))
                .thenReturn(AuthorizationResponse.builder()
                                .code("ABCD")
                                .build());

        AuthorizationRequest input = AuthorizationRequest.builder()
                .clientId("1234")
                .redirectUri("https://pamarin.com/callback?q=AAA")
                .responseType("code")
                .scope("read")
                .build();

        String output = authorizationService.authorize(input);
        String expected = "https://pamarin.com/callback?q=AAA&code=ABCD";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeReturnCodeAndState_whenResponseTypeIsCode() {
        when(authorizationCodeGenerator.generate(any(AuthorizationRequest.class)))
                .thenReturn(AuthorizationResponse.builder()
                                .code("ABCD")
                                .build());

        AuthorizationRequest input = AuthorizationRequest.builder()
                .clientId("1234")
                .redirectUri("https://pamarin.com/callback")
                .responseType("code")
                .scope("read")
                .state("XYZ")
                .build();

        String output = authorizationService.authorize(input);
        String expected = "https://pamarin.com/callback?code=ABCD&state=XYZ";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeReturnToken_whenResponseTypeIsToken() {
        when(accessTokenGenerator.generate(any(AuthorizationRequest.class)))
                .thenReturn(AccessTokenResponse.builder()
                        .accessToken("ABCDEF")
                        .tokenType("bearer")
                        .expiresIn(3600)
                        .build());

        AuthorizationRequest input = AuthorizationRequest.builder()
                .clientId("1234")
                .redirectUri("https://pamarin.com/callback")
                .responseType("token")
                .scope("read")
                .state("XYZ")
                .build();

        String output = authorizationService.authorize(input);
        String expected = "https://pamarin.com/callback#access_token=ABCDEF&state=XYZ&token_type=bearer&expires_in=3600";
        assertThat(output).isEqualTo(expected);
    }
}
