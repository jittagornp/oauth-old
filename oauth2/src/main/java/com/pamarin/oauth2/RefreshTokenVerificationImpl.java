/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.HashBasedToken;
import com.pamarin.oauth2.collection.OAuth2RefreshToken;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepo;
import com.pamarin.oauth2.service.RefreshTokenVerification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/05
 */
@Service
public class RefreshTokenVerificationImpl implements RefreshTokenVerification {

    @Autowired
    private OAuth2RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private HashBasedToken hashBasedToken;

    @Override
    public OAuth2RefreshToken verify(String token) {
        OAuth2RefreshToken output = OAuth2RefreshToken.builder().build();
        if (!hashBasedToken.matches(token, new UserDetailsServiceImpl(refreshTokenRepo, output))) {
            throw new UnauthorizedClientException("Invalid refresh token");
        }
        return output;
    }

    public static class UserDetailsServiceImpl implements UserDetailsService {

        private final OAuth2RefreshTokenRepo refreshTokenRepo;

        private final OAuth2RefreshToken output;

        public UserDetailsServiceImpl(OAuth2RefreshTokenRepo refreshTokenRepo, OAuth2RefreshToken output) {
            this.refreshTokenRepo = refreshTokenRepo;
            this.output = output;
        }

        @Override
        public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
            OAuth2RefreshToken refreshToken = refreshTokenRepo.findByTokenId(id);
            if (refreshToken == null) {
                throw new UsernameNotFoundException("Not found refresh token");
            }
            String[] ignoreProperties = new String[]{"secretKey"};
            BeanUtils.copyProperties(refreshToken, output, ignoreProperties);
            return DefaultUserDetails.builder()
                    .username(refreshToken.getTokenId())
                    .password(refreshToken.getSecretKey())
                    .build();
        }

    }
}
