/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.security.hashing.Hashing;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import com.pamarin.commons.security.hashing.StringSignature;

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
    
    @Mock
    private StringSignature stringSignature;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(hostUrlProvider.provide()).thenReturn("");
        when(stringSignature.sign(any(String.class))).thenReturn("AAAAA");
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
        String expected = "/login?" + input.buildQuerystring() + "&signature=AAAAA";
        assertThat(output).isEqualTo(expected);
    }
}
