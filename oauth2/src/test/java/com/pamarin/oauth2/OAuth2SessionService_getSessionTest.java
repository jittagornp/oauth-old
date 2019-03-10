/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.constant.OAuth2Constant;
import com.pamarin.oauth2.domain.OAuth2AccessToken;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.model.OAuth2Session;
import com.pamarin.oauth2.service.DatabaseSessionSynchronizer;
import com.pamarin.oauth2.service.OAuth2SessionBuilderService;
import com.pamarin.oauth2.service.OAuth2SessionService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author jitta
 */
public class OAuth2SessionService_getSessionTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private OAuth2SessionService oauth2SessionService;

    private HttpServletRequest httpReq;

    private HttpSession httpSession;

    private DatabaseSessionSynchronizer databaseSessionSynchronizer;

    private OAuth2SessionBuilderService sessionBuilderService;

    @Before
    public void before() {
        httpReq = mock(HttpServletRequest.class);
        httpSession = mock(HttpSession.class);
        when(httpReq.getSession()).thenReturn(httpSession);

        databaseSessionSynchronizer = mock(DatabaseSessionSynchronizer.class);
        sessionBuilderService = mock(OAuth2SessionBuilderService.class);
        oauth2SessionService = new OAuth2SessionServiceImpl();

        ReflectionTestUtils.setField(
                oauth2SessionService,
                "databaseSessionSynchronizer",
                databaseSessionSynchronizer
        );

        ReflectionTestUtils.setField(
                oauth2SessionService,
                "sessionBuilderService",
                sessionBuilderService
        );
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

    private OAuth2Session subOAuth2Session() {
        return OAuth2Session.builder()
                .id("session_id-123")
                .build();
    }

    @Test
    public void shouldBeThrowUnauthorizedClientException_whenAccessTokenNotFound() {
        exception.expect(UnauthorizedClientException.class);
        exception.expectMessage("Access token not found.");

        oauth2SessionService.getSession(httpReq);

        verify(databaseSessionSynchronizer, times(1)).synchronize();
    }

    @Test
    public void shouldBeNotBuildOAuth2Session_whenAlreadyHaveSessionAttribute() {

        OAuth2AccessToken accessToken = stubAccessToken();
        OAuth2Session oauth2Session = subOAuth2Session();
        String attributeKey = "oauth2-session:" + accessToken.getClientId();

        //already have session attribute
        when(httpSession.getAttribute(attributeKey)).thenReturn(oauth2Session);

        //access token verification output
        when(httpReq.getAttribute(OAuth2Constant.ACCESS_TOKEN_ATTRIBUTE)).thenReturn(accessToken);

        OAuth2Session output = oauth2SessionService.getSession(httpReq);
        OAuth2Session expected = oauth2Session;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeBuildOAuth2Session_whenDontHaveSessionAttribute() {

        OAuth2AccessToken accessToken = stubAccessToken();
        OAuth2Session oauth2Session = subOAuth2Session();
        String attributeKey = "oauth2-session:" + accessToken.getClientId();

        //dont' have session attribute
        when(httpSession.getAttribute(attributeKey)).thenReturn(null);

        //access token verification output
        when(httpReq.getAttribute(OAuth2Constant.ACCESS_TOKEN_ATTRIBUTE)).thenReturn(accessToken);

        //build new oauth2 session
        when(sessionBuilderService.build(accessToken)).thenReturn(oauth2Session);

        OAuth2Session output = oauth2SessionService.getSession(httpReq);
        OAuth2Session expected = oauth2Session;
        assertThat(output).isEqualTo(expected);

        verify(httpSession).setAttribute(attributeKey, oauth2Session);
    }
}
