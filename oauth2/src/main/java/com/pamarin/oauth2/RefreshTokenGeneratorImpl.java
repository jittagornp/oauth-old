/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.HashBasedToken;
import static com.pamarin.commons.util.DateConverterUtils.convert2LocalDateTime;
import com.pamarin.oauth2.domain.OAuth2RefreshToken;
import com.pamarin.oauth2.domain.OAuth2Token;
import com.pamarin.oauth2.domain.User;
import com.pamarin.oauth2.exception.UserNotFoundException;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepo;
import com.pamarin.oauth2.repository.UserRepo;
import com.pamarin.oauth2.service.RefreshTokenGenerator;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/05
 */
@Service
@Transactional
public class RefreshTokenGeneratorImpl implements RefreshTokenGenerator {

    @Autowired
    private OAuth2RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private HashBasedToken hashBasedToken;

    @Autowired
    private UserRepo userRepo;

    private OAuth2RefreshToken generateRefreshToken(OAuth2Token token) {
        return refreshTokenRepo.save(OAuth2RefreshToken.builder()
                .id(token.getId())
                .userId(token.getUserId())
                .clientId(token.getClientId())
                .sessionId(token.getSessionId())
                .build());
    }

    @Override
    @SuppressWarnings("null")
    public String generate(OAuth2Token token) {
        User user = userRepo.findOne(token.getUserId());
        if (user == null) {
            UserNotFoundException.throwbyUserId(token.getUserId());
        }
        OAuth2RefreshToken refreshToken = generateRefreshToken(token);
        return hashBasedToken.hash(
                DefaultUserDetails.builder()
                        .username(refreshToken.getId())
                        .password(user.getPassword() + refreshToken.getSecretKey())
                        .build(),
                convert2LocalDateTime(new Date(refreshToken.getExpiresAt()))
        );
    }

}
