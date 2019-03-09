/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.domain.OAuth2Approval;
import com.pamarin.oauth2.domain.OAuth2Client;
import com.pamarin.oauth2.exception.OAuth2ClientNotFoundException;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.model.OAuth2Session;
import com.pamarin.oauth2.repository.OAuth2ApprovalRepo;
import com.pamarin.oauth2.repository.OAuth2ClientRepo;
import com.pamarin.oauth2.repository.OAuth2ClientScopeRepo;
import com.pamarin.oauth2.service.AccessTokenVerification;
import com.pamarin.oauth2.service.OAuth2SessionBuilderService;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jitta
 */
@Service
@Transactional
public class OAuth2SessionBuilderServiceImpl implements OAuth2SessionBuilderService {

    @Autowired
    private OAuth2ClientScopeRepo clientScopeRepo;

    @Autowired
    private OAuth2ClientRepo clientRepo;

    @Autowired
    private OAuth2ApprovalRepo approvalRepo;

    @Override
    public OAuth2Session build(AccessTokenVerification.Output output) {
        OAuth2Approval approval = approvalRepo.findOne(new OAuth2Approval.PK(output.getUserId(), output.getClientId()));
        if (approval == null) {
            throw new UnauthorizedClientException("Unauthorized client " + output.getClientId() + " for user " + output.getUserId() + ".");
        }

        OAuth2Client client = clientRepo.findOne(output.getClientId());
        if (client == null) {
            throw new OAuth2ClientNotFoundException("Not found client id " + output.getClientId());
        }

        return OAuth2Session.builder()
                .id(output.getSessionId())
                .issuedAt(output.getIssuedAt())
                .expiresAt(output.getExpiresAt())
                .user(
                        OAuth2Session.User.builder()
                                .id(output.getUserId())
                                .name("นาย สมชาย ใจดี")
                                .authorities(Arrays.asList("sso"))
                                .build()
                )
                .client(OAuth2Session.Client.builder()
                        .id(client.getId())
                        .name(client.getName())
                        .scopes(clientScopeRepo.findScopeByClientId(client.getId()))
                        .build()
                )
                .build();
    }

}
