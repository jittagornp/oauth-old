/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.domain.OAuth2Approval;
import com.pamarin.oauth2.domain.OAuth2ApprovalScope;
import com.pamarin.oauth2.model.ClientDetails;
import com.pamarin.oauth2.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import com.pamarin.oauth2.repository.OAuth2ApprovalRepository;
import com.pamarin.oauth2.repository.OAuth2ApprovalScopeRepository;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Service
@Transactional
public class ApprovalServiceImpl implements ApprovalService {

    @Autowired
    private OAuth2ApprovalRepository approveRepository;

    @Autowired
    private OAuth2ApprovalScopeRepository approvalScopeRepository;

    @Override
    public boolean wasApprovedByUserIdAndClientId(String userId, String clientId) {
        return approveRepository.findOne(OAuth2Approval.PK.builder()
                .userId(userId)
                .clientId(clientId)
                .build()
        ) != null;
    }

    private void saveScope(OAuth2Approval approval, List<String> scopes) {
        approvalScopeRepository.save(scopes.stream().map(s -> OAuth2ApprovalScope.builder()
                .approval(approval)
                .clientId(approval.getId().getClientId())
                .userId(approval.getId().getUserId())
                .scope(s)
                .build()).collect(Collectors.toList()));
    }

    private OAuth2Approval newOAuth2Approval(String userId, String clientId) {
        return OAuth2Approval.builder()
                .id(OAuth2Approval.PK.builder()
                        .userId(userId)
                        .clientId(clientId)
                        .build()
                )
                .build();
    }

    @Override
    public void approvedClientByUserId(ClientDetails details, String userId) {
        saveScope(approveRepository.save(newOAuth2Approval(
                userId,
                details.getClientId()
        )), details.getScopes());
    }

    @Override
    public List<String> findScopeByUserIdAndClientId(String userId, String clientId) {
        return approvalScopeRepository.findScopeByUserIdAndClientId(userId, clientId);
    }

}
