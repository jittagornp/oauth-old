/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.model.ClientDetails;
import java.util.List;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/10
 */
public interface ApprovalService {

    boolean wasApprovedByUserIdAndClientId(String userId, String clientId);

    void approvedClientByUserId(ClientDetails details, String userId);

    List<String> findScopeByUserIdAndClientId(String userId, String clientId);
}
