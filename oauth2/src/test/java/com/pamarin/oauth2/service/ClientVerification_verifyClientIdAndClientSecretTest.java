/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.commons.security.PasswordEncryption;
import com.pamarin.oauth2.exception.InvalidClientIdAndClientSecretException;
import com.pamarin.oauth2.exception.InvalidClientIdException;
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
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/25
 */
public class ClientVerification_verifyClientIdAndClientSecretTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private DefaultClientVerification clientVerification;

    @Mock
    private ClientService clientService;

    @Mock
    private PasswordEncryption passwordEncryption;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldBeThrowInvalidClientIdAndClientSecretException_whenClientIdAndClientSecretIsNull() {

        exception.expect(InvalidClientIdAndClientSecretException.class);
        exception.expectMessage("Required clientId and clientSecret.");

        String clientId = null;
        String clientSecret = null;
        clientVerification.verifyClientIdAndClientSecret(clientId, clientSecret);
    }

    @Test
    public void shouldBeThrowInvalidClientIdAndClientSecretException_whenNotFoundClientSecret() {

        exception.expect(InvalidClientIdException.class);
        exception.expectMessage("Empty clientSecret.");

        when(clientService.findClientSecretByClientId(any(String.class)))
                .thenReturn(null);

        String clientId = "123456";
        String clientSecret = "xyz";
        clientVerification.verifyClientIdAndClientSecret(clientId, clientSecret);
    }

    @Test
    public void shouldBeThrowInvalidClientIdAndClientSecretException_whenInvalidClientSecret() {

        exception.expect(InvalidClientIdAndClientSecretException.class);
        exception.expectMessage("Invalid clientSecret.");

        when(clientService.findClientSecretByClientId(any(String.class)))
                .thenReturn("password");
        when(passwordEncryption.matches(any(String.class), any(String.class)))
                .thenReturn(false);

        String clientId = "123456";
        String clientSecret = "xyz";
        clientVerification.verifyClientIdAndClientSecret(clientId, clientSecret);
    }

    @Test
    public void shouldBeOk_whenValidClientSecret() {
        when(clientService.findClientSecretByClientId(any(String.class)))
                .thenReturn("password");
        when(passwordEncryption.matches(any(String.class), any(String.class)))
                .thenReturn(true);

        String clientId = "123456";
        String clientSecret = "password";
        clientVerification.verifyClientIdAndClientSecret(clientId, clientSecret);
    }
}
