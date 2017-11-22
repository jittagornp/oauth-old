/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.repository.OAuth2AllowDomainRepo;
import com.pamarin.oauth2.service.AllowDomainService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Service
@Transactional
public class AllowDomainServiceImpl implements AllowDomainService {

    @Autowired
    private OAuth2AllowDomainRepo domainRepo;

    @Override
    public List<String> findDomainByClientId(String clientId) {
        return domainRepo.findDomainNameByClientId(clientId);
    }

}
