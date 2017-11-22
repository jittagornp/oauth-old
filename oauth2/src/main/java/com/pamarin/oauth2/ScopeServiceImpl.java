/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.repository.OAuth2ClientScopeRepo;
import com.pamarin.oauth2.service.ScopeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Service
@Transactional
public class ScopeServiceImpl implements ScopeService {

    @Autowired
    private OAuth2ClientScopeRepo clientScopeRepo;

    @Override
    public List<String> findByClientId(String clientId) {
        return clientScopeRepo.findScopeByClientId(clientId);
    }

}
