/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.HashBasedToken;
import static com.pamarin.commons.util.DateConverterUtils.convert2LocalDateTime;
import com.pamarin.oauth2.collection.OAuth2RefreshToken;
import com.pamarin.oauth2.model.OAuth2Token;
import com.pamarin.oauth2.service.RefreshTokenGenerator;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepository;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/05
 */
@Service
@Transactional
public class RefreshTokenGeneratorImpl implements RefreshTokenGenerator {

    private final OAuth2RefreshTokenRepository repository;

    private final HashBasedToken hashBasedToken;

    @Autowired
    public RefreshTokenGeneratorImpl(OAuth2RefreshTokenRepository repository, HashBasedToken hashBasedToken) {
        this.repository = repository;
        this.hashBasedToken = hashBasedToken;
    }

    private OAuth2RefreshToken generateRefreshToken(OAuth2Token token) {
        return repository.save(OAuth2RefreshToken.builder()
                .tokenId(token.getTokenId())
                .userId(token.getUserId())
                .clientId(token.getClientId())
                .sessionId(token.getSessionId())
                .build());
    }

    @Override
    public String generate(OAuth2Token token) {
        OAuth2RefreshToken refreshToken = generateRefreshToken(token);
        return hashBasedToken.hash(
                DefaultUserDetails.builder()
                        .username(refreshToken.getTokenId())
                        .password(refreshToken.getSecretKey())
                        .build(),
                convert2LocalDateTime(new Date(refreshToken.getExpiresAt()))
        );
    }

}
