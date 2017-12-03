/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.Base64RSAEncryption;
import com.pamarin.commons.security.RSAKeyPairs;
import com.pamarin.oauth2.domain.OAuth2AccessToken;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepo;
import com.pamarin.oauth2.service.AccessTokenVerification;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
@Service
public class AccessTokenVerificationImpl implements AccessTokenVerification {

    @Autowired
    @Qualifier("accessTokenKeyPairs")
    private RSAKeyPairs keyPairs;

    @Autowired
    private OAuth2AccessTokenRepo accessTokenRepo;

    @Autowired
    private Base64RSAEncryption base64RSAEncryption;

    @Override
    public Output verify(String accessToken) {
        String id = base64RSAEncryption.decrypt(accessToken, keyPairs.getPublicKey());
        OAuth2AccessToken token = accessTokenRepo.findById(id);
        return Output.builder()
                .id(StringUtils.split(token.getId(), ":")[1])
                .issuedAt(token.getIssuedAt())
                .expiresAt(token.getExpiresAt())
                .userId(token.getUserId())
                .clientId(token.getClientId())
                .build();
    }

}
