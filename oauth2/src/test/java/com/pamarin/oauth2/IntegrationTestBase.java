/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.model.OAuth2RefreshToken;
import com.pamarin.oauth2.repository.OAuth2AllowDomainRepo;
import com.pamarin.oauth2.repository.UserRepo;
import com.pamarin.oauth2.security.CsrfInterceptor;
import com.pamarin.oauth2.security.GetCsrfTokenIntorceptor;
import com.pamarin.commons.security.UserSessionStub;
import com.pamarin.commons.security.LoginSession;
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
import com.pamarin.oauth2.repository.OAuth2ClientRepo;
import com.pamarin.oauth2.repository.OAuth2ClientScopeRepo;
import java.util.Arrays;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepo;
import com.pamarin.oauth2.repository.OAuth2ScopeRepo;
import com.pamarin.oauth2.service.AuthorizeViewModelService;
import com.pamarin.oauth2.service.AuthorizeViewModelService.Model;
import com.pamarin.oauth2.service.AuthorizeViewModelService.Scope;
import java.util.List;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@ActiveProfiles("test")
public class IntegrationTestBase {

    @MockBean
    private OAuth2RefreshTokenRepo refreshTokenRepo;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private GetCsrfTokenIntorceptor getCsrfTokenIntorceptor;

    @MockBean
    private CsrfInterceptor csrfVerificationInterceptor;

    @MockBean
    private LoginSession loginSession;

    @MockBean
    private OAuth2ApprovalRepo approvalRepo;

    @MockBean
    private OAuth2ApprovalScopeRepo approvalScopeRepo;

    @MockBean
    private OAuth2ClientRepo clientRepo;

    @MockBean
    private OAuth2AllowDomainRepo allowDomainRepo;

    @MockBean
    private OAuth2ClientScopeRepo clientScopeRepo;

    @MockBean
    private OAuth2ScopeRepo scopeRepo;

    @MockBean
    private AuthorizeViewModelService authorizeViewModelService;

    private OAuth2RefreshToken stubRefreshToken() {
        return OAuth2RefreshToken.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .userId(1L)
                .username("test")
                .build();
    }

    @Before
    public void mockRefreshTokenRepo() {
        OAuth2RefreshToken refreshToken = stubRefreshToken();
        when(refreshTokenRepo.save(any(OAuth2RefreshToken.class))).thenReturn(refreshToken);
        when(refreshTokenRepo.findById(any(String.class))).thenReturn(refreshToken);
    }

    @Before
    public void mockGetCsrfTokenIntorceptor() throws Exception {
        when(getCsrfTokenIntorceptor.preHandle(
                any(HttpServletRequest.class),
                any(HttpServletResponse.class),
                any(Object.class)
        )).thenReturn(true);
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
        when(loginSession.getUserSession()).thenReturn(UserSessionStub.get());
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
}
