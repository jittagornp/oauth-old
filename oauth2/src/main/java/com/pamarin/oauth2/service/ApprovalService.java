/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.model.ClientDetails;
import java.util.List;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/10
 */
public interface ApprovalService {

    boolean wasApprovedByUserIdAndClientId(Long userId, String clientId);

    void approvedClientByUserId(ClientDetails details, Long userId);

    List<String> findScopeByUserIdAndClientId(Long userId, String clientId);
}
