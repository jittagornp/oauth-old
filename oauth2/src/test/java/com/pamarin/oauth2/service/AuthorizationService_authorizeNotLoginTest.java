/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.provider.HostUrlProvider;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/25
 */
public class AuthorizationService_authorizeNotLoginTest {

    @InjectMocks
    private DefaultAuthorizationService authorizationService;

    @Mock
    private LoginSession loginSession;

    @Mock
    private HostUrlProvider hostUrlProvider;

    @Mock
    private AuthorizationRequestVerification requestVerification;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(hostUrlProvider.provide()).thenReturn("");
    }

    @Test
    public void shouldBeReturnLoginUri_whenNotLogin() {
        when(loginSession.wasCreated()).thenReturn(false);

        AuthorizationRequest input = AuthorizationRequest.builder()
                .clientId("1234")
                .redirectUri("https://pamarin.com/callback")
                .responseType("code")
                .scope("read")
                .state("XYZ")
                .build();

        String output = authorizationService.authorize(input);
        String expected = "/login?" + input.buildQuerystring();
        assertThat(output).isEqualTo(expected);
    }
}
