/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.domain.OAuth2Approval;
import com.pamarin.oauth2.domain.OAuth2ApprovalScope;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.repository.OAuth2ApprovalRepo;
import com.pamarin.oauth2.repository.OAuth2ApprovalScopeRepo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/22
 */
public class ApprovalService_approvedClientByUserIdTest {

    @InjectMocks
    private ApprovalServiceImpl approvalService;

    @Mock
    private OAuth2ApprovalRepo approvalRepo;

    @Mock
    private OAuth2ApprovalScopeRepo approvalScopeRepo;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldBeOk() {
        OAuth2Approval approval = new OAuth2Approval();
        approval.setId(new OAuth2Approval.PK("00000000000000000000000000000000", "1234"));
        when(approvalRepo.save(any(OAuth2Approval.class)))
                .thenReturn(approval);

        approvalService.approvedClientByUserId(AuthorizationRequest.builder()
                .clientId("1234")
                .scope("user:public_profile")
                .build(),
                any(String.class)
        );
        
        verify(approvalRepo).save(Mockito.any(OAuth2Approval.class));
        verify(approvalScopeRepo).save(Mockito.anyListOf(OAuth2ApprovalScope.class));
    }
}
