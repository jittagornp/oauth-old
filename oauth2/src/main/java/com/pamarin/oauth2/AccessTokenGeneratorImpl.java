/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.KeyPairs;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.model.AccessTokenResponse;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.model.CodeAccessTokenRequest;
import com.pamarin.oauth2.model.RefreshAccessTokenRequest;
import com.pamarin.oauth2.model.OAuth2RefreshToken;
import com.pamarin.oauth2.model.TokenBase;
import com.pamarin.oauth2.service.AccessTokenGenerator;
import com.pamarin.oauth2.service.ClientVerification;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.service.TokenVerification;
import static com.pamarin.commons.util.DateConverterUtils.convert2Date;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.pamarin.commons.security.UserSession;
import com.pamarin.oauth2.service.ApprovalService;
import java.util.List;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepo;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Service
@Transactional
class AccessTokenGeneratorImpl implements AccessTokenGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(AccessTokenGeneratorImpl.class);

    private static final int EXPIRES_MINUTES = 15;

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
    private KeyPairs keyPairs;

    @Autowired
    private ApprovalService approvalService;

    private Algorithm getAlgorithm() {
        return Algorithm.RSA256(keyPairs.getRSAPublicKey(), keyPairs.getRSAPrivateKey());
    }

    private String signToken(TokenBase base, List<String> scopes, int expiresMinute) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expires = now.plusMinutes(expiresMinute);
        String[] arr = new String[scopes.size()];
        return JWT.create()
                .withSubject(base.getUsername())
                .withIssuer(String.valueOf(base.getUserId()))
                .withIssuedAt(convert2Date(now))
                .withExpiresAt(convert2Date(expires))
                .withArrayClaim("scopes", scopes.toArray(arr))
                .sign(getAlgorithm());
    }

    private AccessTokenResponse buildAccessTokenResponse(TokenBase base, List<String> scopes, String refreshToken) {
        return AccessTokenResponse.builder()
                .accessToken(signToken(base, scopes, EXPIRES_MINUTES))
                .expiresIn(EXPIRES_MINUTES * 60L)
                .refreshToken(refreshToken)
                .tokenType("bearer")
                .build();
    }

    private AccessTokenResponse buildAccessTokenResponse(TokenBase base, List<String> scopes) {
        return buildAccessTokenResponse(base, scopes, generateRefreshToken(base).getId());
    }

    private OAuth2RefreshToken generateRefreshToken(TokenBase base) {
        return refreshTokenRepo.save(OAuth2RefreshToken.builder()
                .userId(base.getUserId())
                .username(base.getUsername())
                .build());
    }

    @Override
    public AccessTokenResponse generate(AuthorizationRequest req) {
        UserSession userSession = loginSession.getUserSession();
        return buildAccessTokenResponse(
                TokenBase.builder()
                        .userId(userSession.getId())
                        .username(userSession.getUsername())
                        .build(),
                approvalService.findScopeByUserIdAndClientId(
                        userSession.getId(),
                        req.getClientId()
                )
        );
    }

    @Override
    public AccessTokenResponse generate(CodeAccessTokenRequest req) {
        clientVerification.verifyClientIdAndClientSecret(req.getClientId(), req.getClientSecret());
        try {
            TokenBase authCode = authorizationCodeVerification.verify(req.getCode());
            return buildAccessTokenResponse(authCode,
                    approvalService.findScopeByUserIdAndClientId(
                            authCode.getUserId(),
                            req.getClientId()
                    )
            );
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
                .userId(refreshToken.getUserId())
                .username(refreshToken.getUsername())
                .build(),
                approvalService.findScopeByUserIdAndClientId(
                        refreshToken.getUserId(),
                        req.getClientId()
                ),
                refreshToken.getId()
        );
    }
}
