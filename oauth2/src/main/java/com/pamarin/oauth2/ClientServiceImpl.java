/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.repository.OAuth2ClientRepo;
import com.pamarin.oauth2.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    private OAuth2ClientRepo clientRepo;

    @Override
    public String findClientSecretByClientId(String clientId) {
        return clientRepo.findSecretById(clientId);
    }

}
