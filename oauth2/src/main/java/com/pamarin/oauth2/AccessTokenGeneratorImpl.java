/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.HashBasedToken;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.model.AccessTokenResponse;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.model.CodeAccessTokenRequest;
import com.pamarin.oauth2.model.RefreshAccessTokenRequest;
import com.pamarin.oauth2.service.AccessTokenGenerator;
import com.pamarin.oauth2.service.ClientVerification;
import com.pamarin.commons.security.LoginSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import static com.pamarin.commons.util.DateConverterUtils.convert2LocalDateTime;
import com.pamarin.oauth2.collection.OAuth2AccessToken;
import com.pamarin.oauth2.domain.OAuth2AuthorizationCode;
import com.pamarin.oauth2.collection.OAuth2RefreshToken;
import com.pamarin.oauth2.domain.OAuth2Token;
import com.pamarin.oauth2.exception.InvalidTokenException;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepo;
import com.pamarin.oauth2.service.AuthorizationCodeVerification;
import com.pamarin.oauth2.service.RefreshTokenGenerator;
import com.pamarin.oauth2.service.RefreshTokenVerification;
import com.pamarin.oauth2.service.RevokeTokenService;
import java.util.Date;

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
    private AuthorizationCodeVerification authorizationCodeVerification;

    @Autowired
    private ClientVerification clientVerification;

    @Autowired
    private OAuth2AccessTokenRepo accessTokenRepo;

    @Autowired
    private RefreshTokenGenerator refreshTokenGenerator;

    @Autowired
    private RefreshTokenVerification refreshTokenVerification;

    @Autowired
    private HashBasedToken hashBasedToken;

    @Autowired
    private RevokeTokenService revokeTokenService;

    private AccessTokenResponse buildAccessTokenResponse(OAuth2Token instance) {
        OAuth2AccessToken accessToken = accessTokenRepo.save(OAuth2AccessToken.builder()
                .userId(instance.getUserId())
                .clientId(instance.getClientId())
                .sessionId(instance.getSessionId())
                .build()
        );
        String token = hashBasedToken.hash(
                DefaultUserDetails.builder()
                        .username(accessToken.getTokenId())
                        .password(accessToken.getSecretKey())
                        .build(),
                convert2LocalDateTime(new Date(accessToken.getExpiresAt()))
        );
        instance.setTokenId(accessToken.getTokenId());
        return AccessTokenResponse.builder()
                .accessToken(token)
                .expiresIn(accessToken.getExpireMinutes() * 60L)
                .refreshToken(refreshTokenGenerator.generate(instance))
                .tokenType("bearer")
                .build();
    }

    @Override
    public AccessTokenResponse generate(AuthorizationRequest req) {
        UserDetails userDetails = loginSession.getUserDetails();
        return buildAccessTokenResponse(OAuth2AccessToken.builder()
                .clientId(req.getClientId())
                .userId(userDetails.getUsername())
                .sessionId(loginSession.getSessionId())
                .build());
    }

    @Override
    public AccessTokenResponse generate(CodeAccessTokenRequest req) {
        clientVerification.verifyClientIdAndClientSecret(req.getClientId(), req.getClientSecret());
        try {
            OAuth2AuthorizationCode authCode = authorizationCodeVerification.verify(req.getCode());
            return buildAccessTokenResponse(authCode);
        } catch (InvalidTokenException ex) {
            LOG.warn(null, ex);
            throw new UnauthorizedClientException(ex);
        }
    }

    @Override
    public AccessTokenResponse generate(RefreshAccessTokenRequest req) {
        clientVerification.verifyClientIdAndClientSecret(req.getClientId(), req.getClientSecret());
        OAuth2RefreshToken refreshToken = refreshTokenVerification.verify(req.getRefreshToken());
        revokeTokenService.revokeByTokenId(refreshToken.getTokenId());
        refreshToken.setTokenId(null);
        refreshToken.setClientId(req.getClientId());
        return buildAccessTokenResponse(refreshToken);
    }
}
