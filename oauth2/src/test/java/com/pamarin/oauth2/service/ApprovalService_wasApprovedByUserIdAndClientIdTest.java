/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.ApprovalServiceImpl;
import com.pamarin.oauth2.domain.OAuth2Approval;
import com.pamarin.oauth2.repository.OAuth2ApprovalRepo;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/20
 */
public class ApprovalService_wasApprovedByUserIdAndClientIdTest {

    @InjectMocks
    private ApprovalServiceImpl approvalService;

    @Mock
    private OAuth2ApprovalRepo approvalRepo;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldBeFalse_whenNotFoundApproval() {
        when(approvalRepo.findOne(any(OAuth2Approval.PK.class)))
                .thenReturn(null);

        Long userId = 1L;
        String clientId = "1";
        boolean output = approvalService.wasApprovedByUserIdAndClientId(userId, clientId);
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeTrue_whenFoundApproval() {
        OAuth2Approval approval = new OAuth2Approval();
        when(approvalRepo.findOne(any(OAuth2Approval.PK.class)))
                .thenReturn(approval);

        Long userId = 1L;
        String clientId = "1";
        boolean output = approvalService.wasApprovedByUserIdAndClientId(userId, clientId);
        boolean expected = true;
        assertThat(output).isEqualTo(expected);
    }
}
