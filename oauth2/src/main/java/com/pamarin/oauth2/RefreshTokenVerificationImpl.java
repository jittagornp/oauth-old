/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.HashBasedToken;
import com.pamarin.oauth2.domain.OAuth2RefreshToken;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepo;
import com.pamarin.oauth2.service.RefreshTokenVerification;
import org.springframework.beans.BeanUtils;
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

    private UserDetailsService userDetailsService(OAuth2RefreshToken output) {
        return id -> {
            OAuth2RefreshToken refreshToken = refreshTokenRepo.findById(id);
            if (refreshToken == null) {
                throw new UsernameNotFoundException("Not found refresh token");
            }
            String[] ignoreProperties = new String[]{"secretKey"};
            BeanUtils.copyProperties(refreshToken, output, ignoreProperties);
            return DefaultUserDetails.builder()
                    .username(refreshToken.getId())
                    .password(refreshToken.getSecretKey())
                    .build();
        };
    }

    @Override
    public OAuth2RefreshToken verify(String token) {
        OAuth2RefreshToken output = OAuth2RefreshToken.builder().build();
        if (!hashBasedToken.matches(token, userDetailsService(output))) {
            throw new UnauthorizedClientException("Invalid refresh token");
        }
        return output;
    }

}
