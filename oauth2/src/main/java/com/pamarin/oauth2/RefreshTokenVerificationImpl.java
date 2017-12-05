/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.HashBasedToken;
import com.pamarin.oauth2.domain.OAuth2RefreshToken;
import com.pamarin.oauth2.domain.User;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.model.TokenBase;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepo;
import com.pamarin.oauth2.repository.UserRepo;
import com.pamarin.oauth2.service.RefreshTokenVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/05
 */
@Service
@Transactional
public class RefreshTokenVerificationImpl implements RefreshTokenVerification {

    @Autowired
    private OAuth2RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private HashBasedToken hashBasedToken;

    @Autowired
    private UserRepo userRepo;

    private UserDetailsService userDetailsService(TokenBase base) {
        return id -> {
            OAuth2RefreshToken refreshToken = refreshTokenRepo.findById(id);
            if (refreshToken == null) {
                throw new UsernameNotFoundException("Not found refresh token");
            }
            User user = userRepo.findOne(refreshToken.getUserId());
            if (user == null) {
                throw new UsernameNotFoundException(String.format("Not found %s of id \"%s\"",
                        User.class.getSimpleName(),
                        refreshToken.getUserId()
                ));
            }
            base.setId(id);
            base.setClientId(refreshToken.getClientId());
            base.setUserId(refreshToken.getUserId());
            return DefaultUserDetails.builder()
                    .username(refreshToken.getId())
                    .password(user.getPassword() + refreshToken.getSecretKey())
                    .build();
        };
    }

    @Override
    public TokenBase verify(String token) {
        TokenBase base = TokenBase.builder().build();
        if (!hashBasedToken.matches(token, userDetailsService(base))) {
            throw new UnauthorizedClientException("Invalid refresh token");
        }
        return base;
    }

}
