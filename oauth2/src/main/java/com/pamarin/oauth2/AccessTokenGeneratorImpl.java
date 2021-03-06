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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import static com.pamarin.commons.util.DateConverterUtils.convert2LocalDateTime;
import com.pamarin.oauth2.collection.OAuth2AccessToken;
import com.pamarin.oauth2.domain.OAuth2AuthorizationCode;
import com.pamarin.oauth2.collection.OAuth2RefreshToken;
import com.pamarin.oauth2.model.OAuth2Token;
import com.pamarin.oauth2.exception.InvalidTokenException;
import com.pamarin.oauth2.service.AuthorizationCodeVerification;
import com.pamarin.oauth2.service.RefreshTokenGenerator;
import com.pamarin.oauth2.service.RefreshTokenVerification;
import com.pamarin.oauth2.service.RevokeTokenService;
import java.util.Date;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepository;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Service
@Transactional
class AccessTokenGeneratorImpl implements AccessTokenGenerator {

    @Autowired
    private LoginSession loginSession;

    @Autowired
    private AuthorizationCodeVerification authorizationCodeVerification;

    @Autowired
    private ClientVerification clientVerification;

    @Autowired
    private OAuth2AccessTokenRepository accessTokenRepository;

    @Autowired
    private RefreshTokenGenerator refreshTokenGenerator;

    @Autowired
    private RefreshTokenVerification refreshTokenVerification;

    @Autowired
    private HashBasedToken hashBasedToken;

    @Autowired
    private RevokeTokenService revokeTokenService;

    private AccessTokenResponse buildAccessTokenResponse(OAuth2Token token) {
        OAuth2AccessToken accessToken = generateAccessToken(token);
        token.setTokenId(accessToken.getTokenId());
        return AccessTokenResponse.builder()
                .accessToken(hashToken(accessToken))
                .expiresIn(accessToken.getExpireMinutes() * 60L)
                .refreshToken(refreshTokenGenerator.generate(token))
                .tokenType("bearer")
                .build();
    }

    private OAuth2AccessToken generateAccessToken(OAuth2Token token) {
        return accessTokenRepository.save(OAuth2AccessToken.builder()
                .userId(token.getUserId())
                .clientId(token.getClientId())
                .sessionId(token.getSessionId())
                .build()
        );
    }

    private String hashToken(OAuth2AccessToken accessToken) {
        return hashBasedToken.hash(
                DefaultUserDetails.builder()
                        .username(accessToken.getTokenId())
                        .password(accessToken.getSecretKey())
                        .build(),
                convert2LocalDateTime(new Date(accessToken.getExpiresAt()))
        );
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
