/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.exception.AuthenticationException;
import com.pamarin.commons.exception.RSAEncryptionException;
import com.pamarin.commons.security.Base64RSAEncryption;
import com.pamarin.commons.security.RSAKeyPairs;
import com.pamarin.oauth2.domain.OAuth2AccessToken;
import com.pamarin.oauth2.exception.InvalidTokenException;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepo;
import com.pamarin.oauth2.service.AccessTokenVerification;
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
    @SuppressWarnings("null")
    public Output verify(String accessToken) {
        try {
            String id = base64RSAEncryption.decrypt(accessToken, keyPairs.getPublicKey());
            OAuth2AccessToken token = accessTokenRepo.findById(id);
            if (token == null) {
                AuthenticationException.throwByMessage("Access token not found.");
            }
            return Output.builder()
                    .id(token.getId())
                    .issuedAt(token.getIssuedAt())
                    .expiresAt(token.getExpiresAt())
                    .userId(token.getUserId())
                    .clientId(token.getClientId())
                    .build();
        } catch (RSAEncryptionException ex) {
            throw new InvalidTokenException("Invalid to decrypt access token.", ex);
        }
    }

}
