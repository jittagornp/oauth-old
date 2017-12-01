/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.model.ClientDetails;
import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.security.UserDetailsStub;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/23
 */
public class AuthorizationService_approvedTest {

    @InjectMocks
    private DefaultAuthorizationService authorizationService;

    @Mock
    private HostUrlProvider hostUrlProvider;

    @Mock
    private ApprovalService approvalService;

    @Mock
    private LoginSession loginSession;

    @Mock
    private AuthorizationRequestVerification requestVerification;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(hostUrlProvider.provide()).thenReturn("http://localhost");
        when(loginSession.getUserDetails()).thenReturn(UserDetailsStub.get());
    }

    @Test
    public void shouldBeOk() {
        AuthorizationRequest input = AuthorizationRequest.builder()
                .clientId("123456")
                .redirectUri("http://localhost/callback")
                .responseType("code")
                .scope("basic")
                .state("XYZ")
                .build();
        String output = authorizationService.approved(input);
        String expected = "http://localhost/authorize?response_type=code&client_id=123456&redirect_uri=http%3A%2F%2Flocalhost%2Fcallback&scope=basic&state=XYZ";
        assertThat(output).isEqualTo(expected);
        verify(requestVerification).verify(any(AuthorizationRequest.class));
        verify(approvalService).approvedClientByUserId(any(ClientDetails.class), any(String.class));
    }

}
