/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.pamarin.commons.security.Base64RSAEncryption;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.model.AccessTokenResponse;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.model.CodeAccessTokenRequest;
import com.pamarin.oauth2.model.RefreshAccessTokenRequest;
import com.pamarin.oauth2.domain.OAuth2RefreshToken;
import com.pamarin.oauth2.model.TokenBase;
import com.pamarin.oauth2.service.AccessTokenGenerator;
import com.pamarin.oauth2.service.ClientVerification;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.service.TokenVerification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import com.pamarin.commons.security.RSAKeyPairs;
import com.pamarin.oauth2.domain.OAuth2AccessToken;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepo;
import com.pamarin.oauth2.service.RefreshTokenGenerator;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Service
@Transactional
class AccessTokenGeneratorImpl implements AccessTokenGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(AccessTokenGeneratorImpl.class);

    @Autowired
    private LoginSession loginSession;

    @Autowired
    @Qualifier("authorizationCodeVerification")
    private TokenVerification authorizationCodeVerification;

    @Autowired
    private ClientVerification clientVerification;

    @Autowired
    private OAuth2RefreshTokenRepo refreshTokenRepo;

    @Autowired
    @Qualifier("accessTokenKeyPairs")
    private RSAKeyPairs keyPairs;

    @Autowired
    private OAuth2AccessTokenRepo accessTokenRepo;

    @Autowired
    private Base64RSAEncryption base64RSAEncryption;
    
    @Autowired
    private RefreshTokenGenerator refreshTokenGenerator;

    private AccessTokenResponse buildAccessTokenResponse(TokenBase base, String refreshToken) {
        OAuth2AccessToken accessToken = accessTokenRepo.save(OAuth2AccessToken.builder()
                .userId(base.getUserId())
                .clientId(base.getClientId())
                .build()
        );
        String encryptedToken =  base64RSAEncryption.encrypt(accessToken.getId(), keyPairs.getPrivateKey());
        return AccessTokenResponse.builder()
                .accessToken(encryptedToken)
                .expiresIn(accessToken.getExpireMinutes() * 60L)
                .refreshToken(refreshToken)
                .tokenType("bearer")
                .build();
    }

    private AccessTokenResponse buildAccessTokenResponse(TokenBase base) {
        return buildAccessTokenResponse(base, refreshTokenGenerator.generate(base));
    }

    @Override
    public AccessTokenResponse generate(AuthorizationRequest req) {
        UserDetails userDetails = loginSession.getUserDetails();
        return buildAccessTokenResponse(TokenBase.builder()
                .clientId(req.getClientId())
                .userId(userDetails.getUsername())
                .build());
    }

    @Override
    public AccessTokenResponse generate(CodeAccessTokenRequest req) {
        clientVerification.verifyClientIdAndClientSecret(req.getClientId(), req.getClientSecret());
        try {
            TokenBase authCode = authorizationCodeVerification.verify(req.getCode());
            return buildAccessTokenResponse(authCode);
        } catch (TokenExpiredException ex) {
            LOG.warn(null, ex);
            throw new UnauthorizedClientException(ex);
        }
    }

    @Override
    public AccessTokenResponse generate(RefreshAccessTokenRequest req) {
        clientVerification.verifyClientIdAndClientSecret(req.getClientId(), req.getClientSecret());
        OAuth2RefreshToken refreshToken = refreshTokenRepo.findById(req.getRefreshToken());
        if (refreshToken == null) {
            throw new UnauthorizedClientException("Refresh token not found.");
        }
        //update refresh token expires time
        refreshTokenRepo.save(refreshToken);
        return buildAccessTokenResponse(TokenBase.builder()
                .clientId(req.getClientId())
                .userId(refreshToken.getUserId())
                .build(),
                refreshToken.getId()
        );
    }
}
