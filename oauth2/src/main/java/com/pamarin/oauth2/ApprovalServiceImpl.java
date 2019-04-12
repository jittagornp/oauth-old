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
        OAuth2Approval approval = approveRepository.findOne(new OAuth2Approval.PK(userId, clientId));
        return approval != null;
    }

    private void saveScope(OAuth2Approval approval, List<String> scopes) {
        approvalScopeRepository.save(scopes.stream().map(s -> {
            OAuth2ApprovalScope scope = new OAuth2ApprovalScope();
            scope.setApproval(approval);
            scope.setClientId(approval.getId().getClientId());
            scope.setUserId(approval.getId().getUserId());
            scope.setScope(s);
            return scope;
        }).collect(Collectors.toList()));
    }

    private OAuth2Approval newOAuth2Approval(String userId, String clientId) {
        OAuth2Approval approval = new OAuth2Approval();
        approval.setId(new OAuth2Approval.PK(userId, clientId));
        return approval;
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
