/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.exception.InvalidClientIdException;
import com.pamarin.oauth2.exception.InvalidScopeException;
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
 * create : 2017/10/03
 */
public class ScopeVerificationTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private DefaultScopeVerification scopeVerification;

    @Mock
    private ScopeService scopeService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldBeThrowInvalidScopeException_whenInputIsNull() {

        exception.expect(InvalidScopeException.class);
        exception.expectMessage("Required clientId and scope.");

        String clientId = null;
        String scope = null;

        scopeVerification.verifyByClientIdAndScope(clientId, scope);

    }

    @Test
    public void shouldBeThrowInvalidScopeException_whenInputIsNullOrEmptyString1() {

        exception.expect(InvalidScopeException.class);
        exception.expectMessage("Required clientId and scope.");

        String clientId = null;
        String scope = "";

        scopeVerification.verifyByClientIdAndScope(clientId, scope);

    }

    @Test
    public void shouldBeThrowInvalidScopeException_whenInputIsNullOrEmptyString2() {

        exception.expect(InvalidScopeException.class);
        exception.expectMessage("Required clientId and scope.");

        String clientId = "";
        String scope = null;

        scopeVerification.verifyByClientIdAndScope(clientId, scope);

    }

    @Test
    public void shouldBeThrowInvalidScopeException_whenScopeNotInList() {

        exception.expect(InvalidScopeException.class);
        exception.expectMessage("Invalid scope \"delete\", it's not in [\"read\", \"write\", \"execute\"].");

        when(scopeService.findByClientId(any(String.class)))
                .thenReturn(Arrays.asList("read", "write", "execute"));

        String clientId = "123456";
        String scope = "delete";

        scopeVerification.verifyByClientIdAndScope(clientId, scope);

    }

    @Test
    public void shouldBeThrowInvalidClientIdException_whenEmptyScopes() {

        exception.expect(InvalidClientIdException.class);
        exception.expectMessage("Empty scopes.");

        when(scopeService.findByClientId(any(String.class)))
                .thenReturn(Collections.emptyList());

        String clientId = "123456";
        String scope = "read";

        scopeVerification.verifyByClientIdAndScope(clientId, scope);

    }

    @Test
    public void shouldBeOk_whenValidScope() {
        when(scopeService.findByClientId(any(String.class)))
                .thenReturn(Arrays.asList("read"));

        String clientId = "123456";
        String scope = "read";

        scopeVerification.verifyByClientIdAndScope(clientId, scope);

    }

    @Test
    public void shouldBeOk_whenValidMultipleScope() {
        when(scopeService.findByClientId(any(String.class)))
                .thenReturn(Arrays.asList("read", "write", "execute"));

        String clientId = "123456";
        String scope = "read,write";

        scopeVerification.verifyByClientIdAndScope(clientId, scope);

    }

}
