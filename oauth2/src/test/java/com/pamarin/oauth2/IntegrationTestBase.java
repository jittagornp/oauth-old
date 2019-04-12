/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.oauth2.collection.OAuth2RefreshToken;
import com.pamarin.commons.security.CsrfInterceptor;
import com.pamarin.commons.security.UserDetailsStub;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.cache.OAuth2SessionCacheStore;
import com.pamarin.oauth2.collection.OAuth2AccessToken;
import com.pamarin.oauth2.domain.OAuth2AuthorizationCode;
import com.pamarin.oauth2.domain.User;
import com.pamarin.oauth2.repository.mongodb.MongodbOAuth2AccessTokenRepository;
import com.pamarin.oauth2.repository.mongodb.MongodbOAuth2RefreshTokenRepository;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import com.pamarin.oauth2.repository.OAuth2JobSchedulerRepository;
import java.util.Arrays;
import com.pamarin.oauth2.service.AuthorizeViewModelService;
import com.pamarin.oauth2.service.AuthorizeViewModelService.Model;
import com.pamarin.oauth2.service.AuthorizeViewModelService.Scope;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.session.SessionRepository;
import com.pamarin.oauth2.service.RevokeTokenService;
import javax.sql.DataSource;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepository;
import com.pamarin.oauth2.repository.OAuth2AllowDomainRepository;
import com.pamarin.oauth2.repository.OAuth2ApprovalRepository;
import com.pamarin.oauth2.repository.OAuth2ApprovalScopeRepository;
import com.pamarin.oauth2.repository.OAuth2AuthorizationCodeRepository;
import com.pamarin.oauth2.repository.OAuth2ClientRepository;
import com.pamarin.oauth2.repository.OAuth2ClientScopeRepository;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepository;
import com.pamarin.oauth2.repository.OAuth2ScopeRepository;
import com.pamarin.oauth2.repository.UserAgentRepository;
import com.pamarin.oauth2.repository.UserRepository;
import com.pamarin.oauth2.repository.UserSessionRepository;
import com.pamarin.oauth2.repository.mongodb.LoginHistoryRepository;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@ActiveProfiles("test")
public class IntegrationTestBase {
    
    @MockBean 
    private DataSource dataSource; 

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected UserSessionRepository userSessionRepository;
    
    @MockBean
    protected OAuth2JobSchedulerRepository jobSchedulerRepository;

    @MockBean
    protected UserAgentRepository userAgentRepository;

    @MockBean
    protected CsrfInterceptor csrfVerificationInterceptor;

    @MockBean
    protected LoginSession loginSession;

    @MockBean
    protected OAuth2ApprovalRepository approvalRepository;

    @MockBean
    protected OAuth2ApprovalScopeRepository approvalScopeRepository;

    @MockBean
    protected OAuth2ClientRepository clientRepository;

    @MockBean
    protected OAuth2AllowDomainRepository allowDomainRepository;

    @MockBean
    protected OAuth2ClientScopeRepository clientScopeRepository;

    @MockBean
    protected OAuth2ScopeRepository scopeRepository;

    @MockBean
    protected AuthorizeViewModelService authorizeViewModelService;

    @MockBean
    protected OAuth2AuthorizationCodeRepository authorizationCodeRepository;

    @MockBean
    protected OAuth2AccessTokenRepository accessTokenRepository;

    @MockBean
    protected OAuth2RefreshTokenRepository refreshTokenRepository;

    @MockBean
    protected OAuth2SessionCacheStore sessionCacheStore;

    @MockBean
    protected HttpServletRequestProvider httpServletRequestProvider;

    @MockBean
    protected SessionRepository sessionRepository;

    @MockBean
    protected LoginHistoryRepository loginHistoryRepository;

    @MockBean
    protected MongodbOAuth2AccessTokenRepository mongodbOAuth2AccessTokenRepository;

    @MockBean
    protected MongodbOAuth2RefreshTokenRepository mongodbOAuth2RefreshTokenRepository;
    
    @MockBean
    protected RevokeTokenService revokeTokenService;

    private OAuth2RefreshToken stubRefreshToken() {
        return OAuth2RefreshToken.builder()
                .tokenId(UUID.randomUUID().toString().replace("-", ""))
                .userId("00000000000000000000000000000000")
                .clientId("00000000000000000000000000000000")
                .build();
    }

    @Before
    public void mockUserRepository() {
        UserDetails userDetails = UserDetailsStub.get();
        User user = new User();
        user.setId(userDetails.getUsername());
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        when(userRepository.findOne(any(String.class)))
                .thenReturn(user);
    }

    @Before
    public void mockRefreshTokenRepository() {
        OAuth2RefreshToken refreshToken = stubRefreshToken();
        when(refreshTokenRepository.save(any(OAuth2RefreshToken.class))).thenReturn(refreshToken);
        when(refreshTokenRepository.findByTokenId(any(String.class))).thenReturn(refreshToken);
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
    public void mockClientRepository() {
        when(clientRepository.findSecretById(any(String.class)))
                .thenReturn("password");
    }

    @Before
    public void mockAllowDomainRepository() {
        when(allowDomainRepository.findDomainNameByClientId(any(String.class)))
                .thenReturn(Arrays.asList("http://localhost"));
    }

    @Before
    public void mockClientScopeRepository() {
        when(clientScopeRepository.findScopeByClientId(any(String.class)))
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
    public void mockOAuth2AccessTokenRepository() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expires = now.plusMinutes(30);
        when(accessTokenRepository.save(any(OAuth2AccessToken.class)))
                .thenReturn(OAuth2AccessToken.builder()
                        .tokenId("abcdefghijklmnopqrstuvwxyz")
                        .userId(UUID.randomUUID().toString())
                        .clientId(UUID.randomUUID().toString())
                        .issuedAt(Timestamp.valueOf(now).getTime())
                        .expiresAt(Timestamp.valueOf(expires).getTime())
                        .build());
    }

    @Before
    public void mockOAuth2AuthorizationCodeRepository() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expires = now.plusMinutes(30);
        when(authorizationCodeRepository.save(any(OAuth2AuthorizationCode.class)))
                .thenReturn(OAuth2AuthorizationCode.builder()
                        .tokenId("00000000-0000-0000-0000-000000000000")
                        .userId(UUID.randomUUID().toString())
                        .clientId(UUID.randomUUID().toString())
                        .issuedAt(Timestamp.valueOf(now).getTime())
                        .expiresAt(Timestamp.valueOf(expires).getTime())
                        .build());
    }
}
