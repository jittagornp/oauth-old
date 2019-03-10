/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.oauth2.domain.OAuth2RefreshToken;
import com.pamarin.oauth2.repository.OAuth2AllowDomainRepo;
import com.pamarin.oauth2.repository.UserRepo;
import com.pamarin.commons.security.CsrfInterceptor;
import com.pamarin.commons.security.UserDetailsStub;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.cache.OAuth2SessionCacheStore;
import com.pamarin.oauth2.domain.OAuth2AccessToken;
import com.pamarin.oauth2.domain.OAuth2AuthorizationCode;
import com.pamarin.oauth2.domain.User;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepo;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import com.pamarin.oauth2.repository.OAuth2ApprovalRepo;
import com.pamarin.oauth2.repository.OAuth2ApprovalScopeRepo;
import com.pamarin.oauth2.repository.OAuth2AuthorizationCodeRepo;
import com.pamarin.oauth2.repository.OAuth2ClientRepo;
import com.pamarin.oauth2.repository.OAuth2ClientScopeRepo;
import java.util.Arrays;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepo;
import com.pamarin.oauth2.repository.OAuth2ScopeRepo;
import com.pamarin.oauth2.repository.UserSessionRepo;
import com.pamarin.oauth2.service.AuthorizeViewModelService;
import com.pamarin.oauth2.service.AuthorizeViewModelService.Model;
import com.pamarin.oauth2.service.AuthorizeViewModelService.Scope;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import com.pamarin.oauth2.repository.UserSourceRepo;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@ActiveProfiles("test")
public class IntegrationTestBase {

    @MockBean
    protected UserRepo userRepo;

    @MockBean
    protected UserSessionRepo userSessionRepo;

    @MockBean
    protected UserSourceRepo userSourceRepo;

    @MockBean
    protected CsrfInterceptor csrfVerificationInterceptor;

    @MockBean
    protected LoginSession loginSession;

    @MockBean
    protected OAuth2ApprovalRepo approvalRepo;

    @MockBean
    protected OAuth2ApprovalScopeRepo approvalScopeRepo;

    @MockBean
    protected OAuth2ClientRepo clientRepo;

    @MockBean
    protected OAuth2AllowDomainRepo allowDomainRepo;

    @MockBean
    protected OAuth2ClientScopeRepo clientScopeRepo;

    @MockBean
    protected OAuth2ScopeRepo scopeRepo;

    @MockBean
    protected AuthorizeViewModelService authorizeViewModelService;

    @MockBean
    protected OAuth2AuthorizationCodeRepo authorizationCodeRepo;

    @MockBean
    protected OAuth2AccessTokenRepo accessTokenRepo;

    @MockBean
    protected OAuth2RefreshTokenRepo refreshTokenRepo;

    @MockBean
    protected OAuth2SessionCacheStore sessionCacheStore;

    @MockBean
    protected HttpServletRequestProvider httpServletRequestProvider;

    private OAuth2RefreshToken stubRefreshToken() {
        return OAuth2RefreshToken.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .userId("00000000000000000000000000000000")
                .clientId("00000000000000000000000000000000")
                .build();
    }

    @Before
    public void mockUserRepo() {
        UserDetails userDetails = UserDetailsStub.get();
        User user = new User();
        user.setId(userDetails.getUsername());
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        when(userRepo.findOne(any(String.class)))
                .thenReturn(user);
    }

    @Before
    public void mockRefreshTokenRepo() {
        OAuth2RefreshToken refreshToken = stubRefreshToken();
        when(refreshTokenRepo.save(any(OAuth2RefreshToken.class))).thenReturn(refreshToken);
        when(refreshTokenRepo.findById(any(String.class))).thenReturn(refreshToken);
    }

    @Before
    public void mockCsrfVerificationInterceptor() throws Exception {
        when(csrfVerificationInterceptor.preHandle(
                any(HttpServletRequest.class),
                any(HttpServletResponse.class),
                any(Object.class)
        )).thenReturn(true);
    }

    @Before
    public void mockLoginSession() {
        when(loginSession.getUserDetails()).thenReturn(UserDetailsStub.get());
    }

    @Before
    public void mockClientRepo() {
        when(clientRepo.findSecretById(any(String.class)))
                .thenReturn("password");
    }

    @Before
    public void mockAllowDomainRepo() {
        when(allowDomainRepo.findDomainNameByClientId(any(String.class)))
                .thenReturn(Arrays.asList("http://localhost"));
    }

    @Before
    public void mockClientScopeRepo() {
        when(clientScopeRepo.findScopeByClientId(any(String.class)))
                .thenReturn(Arrays.asList("read"));
    }

    @Before
    public void mockAuthorizeViewModelService() {
        when(authorizeViewModelService.findByClientIdAndScopes(any(String.class), any(List.class)))
                .thenReturn(Model.builder()
                        .clientName("test client")
                        .userName("test")
                        .scopes(Arrays.asList(Scope.builder().id("user:public_profile").description("โปรไฟล์สาธารณะ").build()))
                        .build());
    }

    @Before
    public void mockOAuth2AccessTokenRepo() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expires = now.plusMinutes(30);
        when(accessTokenRepo.save(any(OAuth2AccessToken.class)))
                .thenReturn(OAuth2AccessToken.builder()
                        .id("abcdefghijklmnopqrstuvwxyz")
                        .userId(UUID.randomUUID().toString())
                        .clientId(UUID.randomUUID().toString())
                        .issuedAt(Timestamp.valueOf(now).getTime())
                        .expiresAt(Timestamp.valueOf(expires).getTime())
                        .build());
    }

    @Before
    public void mockOAuth2AuthorizationCodeRepo() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expires = now.plusMinutes(30);
        when(authorizationCodeRepo.save(any(OAuth2AuthorizationCode.class)))
                .thenReturn(OAuth2AuthorizationCode.builder()
                        .id("00000000-0000-0000-0000-000000000000")
                        .userId(UUID.randomUUID().toString())
                        .clientId(UUID.randomUUID().toString())
                        .issuedAt(Timestamp.valueOf(now).getTime())
                        .expiresAt(Timestamp.valueOf(expires).getTime())
                        .build());
    }
}
