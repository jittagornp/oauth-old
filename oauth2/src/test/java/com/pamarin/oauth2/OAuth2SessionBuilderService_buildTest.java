/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.session.OAuth2SessionBuilderServiceImpl;
import com.pamarin.oauth2.collection.OAuth2AccessToken;
import com.pamarin.oauth2.domain.OAuth2Approval;
import com.pamarin.oauth2.domain.OAuth2Client;
import com.pamarin.oauth2.exception.OAuth2ClientNotFoundException;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.model.OAuth2Session;
import com.pamarin.oauth2.service.AccessTokenVerification;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import com.pamarin.oauth2.repository.OAuth2ApprovalRepository;
import com.pamarin.oauth2.repository.OAuth2ClientRepository;
import com.pamarin.oauth2.repository.OAuth2ClientScopeRepository;

/**
 *
 * @author jitta
 */
@RunWith(SpringRunner.class)
public class OAuth2SessionBuilderService_buildTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private OAuth2SessionBuilderServiceImpl builderService;

    @MockBean
    private OAuth2ApprovalRepository approvalRepository;

    @MockBean
    private OAuth2ClientScopeRepository clientScopeRepository;

    @MockBean
    private OAuth2ClientRepository clientRepository;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    private OAuth2AccessToken stubAccessToken() {
        String clientId = "client_id-123";
        String userId = "user_id-123";
        String sessionId = "session_id-123";
        return OAuth2AccessToken.builder()
                .clientId(clientId)
                .userId(userId)
                .sessionId(sessionId)
                .build();
    }

    @Test
    public void shouldBeThrowUnauthorizedClientException_whenOAuth2ApprovalIsNull() {

        OAuth2AccessToken accessToken = stubAccessToken();

        when(approvalRepository.findOne(new OAuth2Approval.PK(
                accessToken.getUserId(),
                accessToken.getClientId()
        ))).thenReturn(null);

        exception.expect(UnauthorizedClientException.class);
        exception.expectMessage("Unauthorized client " + accessToken.getClientId() + " for user " + accessToken.getUserId() + ".");

        builderService.build(accessToken);

    }

    @Test
    public void shouldBeThrowOAuth2ClientNotFoundException_whenOAuth2ClientIsNull() {

        OAuth2AccessToken accessToken = stubAccessToken();

        when(approvalRepository.findOne(new OAuth2Approval.PK(
                accessToken.getUserId(),
                accessToken.getClientId()
        ))).thenReturn(new OAuth2Approval());

        when(clientRepository.findOne(accessToken.getClientId())).thenReturn(null);

        exception.expect(OAuth2ClientNotFoundException.class);
        exception.expectMessage("Not found client id " + accessToken.getClientId());

        builderService.build(accessToken);

    }

    @Test
    public void shouldBeOk() {

        OAuth2AccessToken accessToken = stubAccessToken();

        when(approvalRepository.findOne(new OAuth2Approval.PK(
                accessToken.getUserId(),
                accessToken.getClientId()
        ))).thenReturn(new OAuth2Approval());

        when(clientRepository.findOne(accessToken.getClientId()))
                .thenReturn(new OAuth2Client());

        OAuth2Session output = builderService.build(accessToken);
        assertThat(output).isNotNull();

    }

}
