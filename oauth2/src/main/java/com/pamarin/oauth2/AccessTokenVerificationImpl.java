/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.HashBasedToken;
import com.pamarin.oauth2.domain.OAuth2AccessToken;
import com.pamarin.oauth2.exception.InvalidTokenException;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepo;
import com.pamarin.oauth2.service.AccessTokenVerification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
@Service
public class AccessTokenVerificationImpl implements AccessTokenVerification {

    @Autowired
    private OAuth2AccessTokenRepo accessTokenRepo;

    @Autowired
    private HashBasedToken hashBasedToken;

    @Override
    public OAuth2AccessToken verify(String accessToken) {
        OAuth2AccessToken output = OAuth2AccessToken.builder().build();
        if (!hashBasedToken.matches(accessToken, new UserDetailsServiceImpl(accessTokenRepo, output))) {
            throw new InvalidTokenException("Invalid access token.");
        }
        return output;
    }

    public static class UserDetailsServiceImpl implements UserDetailsService {

        private final OAuth2AccessTokenRepo accessTokenRepo;

        private final OAuth2AccessToken output;

        public UserDetailsServiceImpl(OAuth2AccessTokenRepo accessTokenRepo, OAuth2AccessToken output) {
            this.accessTokenRepo = accessTokenRepo;
            this.output = output;
        }

        @Override
        public UserDetails loadUserByUsername(String tokenId) throws UsernameNotFoundException {
            OAuth2AccessToken token = accessTokenRepo.findByTokenId(tokenId);
            if (token == null) {
                throw new UsernameNotFoundException("Not found access token");
            }

            String[] ignoreProperties = new String[]{"secretKey"};
            BeanUtils.copyProperties(token, output, ignoreProperties);
            return DefaultUserDetails.builder()
                    .username(token.getTokenId())
                    .password(token.getSecretKey())
                    .build();
        }

    }
}
