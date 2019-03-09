/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.exception.InvalidResponseTypeException;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.validator.ResponseType;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/12
 */
public class AuthorizationRequestVerificationTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private DefaultAuthorizationRequestVerification requestVerification;

    @Mock
    private ResponseType.Validator responseTypeValidator;

    @Mock
    private ClientVerification clientVerification;

    @Mock
    private ScopeVerification scopeVerification;

    @Mock
    private DefaultScope defaultScope;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(defaultScope.getDefault()).thenReturn("basic");
    }

    @Test
    public void shouldBeThrowInvalidResponseTypeException_whenInvalidResponseType() {

        exception.expect(InvalidResponseTypeException.class);
        exception.expectMessage("Invalid responseType");

        requestVerification.verify(AuthorizationRequest.builder().build());

    }

    @Test
    public void shouldBeCallClientAndScopeVerification() {
        assertThat(defaultScope.getDefault()).isEqualTo("basic");
        when(responseTypeValidator.isValid(any(String.class))).thenReturn(true);

        AuthorizationRequest input = AuthorizationRequest.builder().build();
        requestVerification.verify(input);

        assertThat(input.getScope()).isEqualTo("basic");

        verify(clientVerification).verifyClientIdAndRedirectUri(null, null);
        verify(scopeVerification).verifyByClientIdAndScope(null, "basic");
    }

    @Test
    public void shouldBeNeverCallSetScope_whenScopeIsWrite() {
        when(responseTypeValidator.isValid(any(String.class))).thenReturn(true);

        AuthorizationRequest input = AuthorizationRequest.builder()
                .scope("write")
                .build();
        requestVerification.verify(input);

        verify(defaultScope, never()).getDefault();
        verify(clientVerification).verifyClientIdAndRedirectUri(null, null);
        verify(scopeVerification).verifyByClientIdAndScope(null, "write");
    }
}
