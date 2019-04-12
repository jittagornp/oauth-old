/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.domain.OAuth2Client;
import com.pamarin.oauth2.domain.OAuth2Scope;
import com.pamarin.oauth2.exception.OAuth2ClientNotFoundException;
import com.pamarin.oauth2.exception.OAuth2ScopeNotFoundExpception;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.service.AuthorizeViewModelService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pamarin.oauth2.repository.OAuth2ClientRepository;
import com.pamarin.oauth2.repository.OAuth2ScopeRepository;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/22
 */
@Service
@Transactional
public class AuthorizeViewModelServiceImpl implements AuthorizeViewModelService {

    @Autowired
    private OAuth2ClientRepository clientRepository;

    @Autowired
    private LoginSession loginSession;

    @Autowired
    private OAuth2ScopeRepository scopeRepository;

    @SuppressWarnings("null")
    private String findClientNameById(String clientId) {
        OAuth2Client client = clientRepository.findOne(clientId);
        if (client == null) {
            OAuth2ClientNotFoundException.throwByClientId(clientId);
        }

        return client.getName();
    }

    private String findUserName() {
        return loginSession.getUserDetails().getUsername();
    }

    @SuppressWarnings("null")
    private List<Scope> convert2Scope(List<String> scopes) {
        return scopes.stream().map(s -> {
            OAuth2Scope scope = scopeRepository.findOne(s);
            if (scope == null) {
                OAuth2ScopeNotFoundExpception.throwByScope(s);
            }
            return Scope.builder()
                    .id(scope.getId())
                    .description(scope.getDescription())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public Model findByClientIdAndScopes(String clientId, List<String> scopes) {
        return Model.builder()
                .clientName(findClientNameById(clientId))
                .userName(findUserName())
                .scopes(convert2Scope(scopes))
                .build();
    }

}
