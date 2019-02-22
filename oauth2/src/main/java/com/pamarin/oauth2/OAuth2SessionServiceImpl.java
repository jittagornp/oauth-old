/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.cache.OAuth2SessionCacheStore;
import com.pamarin.oauth2.domain.OAuth2Client;
import com.pamarin.oauth2.model.OAuth2Session;
import com.pamarin.oauth2.repository.OAuth2ClientRepo;
import com.pamarin.oauth2.repository.OAuth2ClientScopeRepo;
import com.pamarin.oauth2.service.AccessTokenVerification;
import com.pamarin.oauth2.service.OAuth2SessionService;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jitta
 */
@Service
@Transactional
public class OAuth2SessionServiceImpl implements OAuth2SessionService {

    private static final Logger LOG = LoggerFactory.getLogger(OAuth2SessionServiceImpl.class);

    @Autowired
    private OAuth2ClientScopeRepo clientScopeRepo;

    @Autowired
    private OAuth2ClientRepo clientRepo;

    @Autowired
    private AccessTokenVerification accessTokenVerification;

    @Autowired
    private OAuth2SessionCacheStore cacheStore;

    @Override
    public OAuth2Session getSession(String accessToken) {
        AccessTokenVerification.Output output = accessTokenVerification.verify(accessToken);
        OAuth2Session session = cacheStore.get(output.getId());
        if (session == null) {
            OAuth2Client client = clientRepo.findOne(output.getClientId());
            session = OAuth2Session.builder()
                    .id(output.getId())
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
                            .id(client == null ? null : client.getId())
                            .name(client == null ? null : client.getName())
                            .scopes(client == null ? null : clientScopeRepo.findScopeByClientId(client.getId()))
                            .build()
                    )
                    .session(output.getSessionId())
                    .build();
            cacheStore.cache(output.getId(), session);
        }
        return session;
    }

}
