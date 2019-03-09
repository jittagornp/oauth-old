/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.exception.InvalidClientIdAndRedirectUriException;
import com.pamarin.oauth2.exception.InvalidClientIdException;
import com.pamarin.oauth2.exception.InvalidRedirectUriException;
import com.pamarin.commons.validator.ValidUri;
import java.util.Arrays;
import java.util.Collections;
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
public class ClientVerification_verifyClientIdAndRedirectUriTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private DefaultClientVerification clientVerification;

    @Mock
    private AllowDomainService allowDomainService;

    @Mock
    private ValidUri.Validator validUriValidator;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldBeThrowInvalidClientIdAndRedirectUriException_whenClientIdAndRedirectUriIsNull() {
        exception.expect(InvalidClientIdAndRedirectUriException.class);
        exception.expectMessage("Required clientId and redirectUri.");

        String clientId = null;
        String redirectUri = null;
        clientVerification.verifyClientIdAndRedirectUri(clientId, redirectUri);
    }

    @Test
    public void shouldBeThrowInvalidRedirectUriException_whenRedirectUriNotValid() {

        exception.expect(InvalidRedirectUriException.class);
        exception.expectMessage("Invalid redirect uri.");

        when(validUriValidator.isValid(any(String.class))).thenReturn(false);

        String clientId = "123456";
        String redirectUri = "/callback";
        clientVerification.verifyClientIdAndRedirectUri(clientId, redirectUri);
    }

    @Test
    public void shouldBeThrowInvalidClientIdException_whenEmptyAllowDomains() {

        exception.expect(InvalidClientIdException.class);
        exception.expectMessage("Empty allow domains.");

        when(validUriValidator.isValid(any(String.class))).thenReturn(true);
        when(allowDomainService.findDomainByClientId(any(String.class)))
                .thenReturn(Collections.emptyList());

        String clientId = "123456";
        String redirectUri = "https://google.com";
        clientVerification.verifyClientIdAndRedirectUri(clientId, redirectUri);
    }

    @Test
    public void shouldBeThrowInvalidClientIdAndRedirectUriException_whenInvalidRedirectUri() {

        exception.expect(InvalidClientIdAndRedirectUriException.class);
        exception.expectMessage("Invalid Domains.");

        when(validUriValidator.isValid(any(String.class))).thenReturn(true);
        when(allowDomainService.findDomainByClientId(any(String.class)))
                .thenReturn(Arrays.asList("https://pamarin.com"));

        String clientId = "123456";
        String redirectUri = "https://google.com";
        clientVerification.verifyClientIdAndRedirectUri(clientId, redirectUri);
    }

    @Test
    public void shouldBeThrowInvalidClientIdAndRedirectUriException_whenInvalidRedirectUriPath() {

        exception.expect(InvalidClientIdAndRedirectUriException.class);
        exception.expectMessage("Invalid Domains.");

        when(validUriValidator.isValid(any(String.class))).thenReturn(true);
        when(allowDomainService.findDomainByClientId(any(String.class)))
                .thenReturn(Arrays.asList("https://pamarin.com/callback"));

        String clientId = "123456";
        String redirectUri = "https://pamarin.com";
        clientVerification.verifyClientIdAndRedirectUri(clientId, redirectUri);
    }

    @Test
    public void shouldBeOk_whenValidRedirectUri() {

        when(validUriValidator.isValid(any(String.class))).thenReturn(true);
        when(allowDomainService.findDomainByClientId(any(String.class)))
                .thenReturn(Arrays.asList(
                        "https://pamarin.com",
                        "https://google.com"
                ));

        String clientId = "123456";
        String redirectUri = "https://google.com";
        clientVerification.verifyClientIdAndRedirectUri(clientId, redirectUri);
    }

}
