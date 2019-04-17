/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.HashBasedToken;
import com.pamarin.oauth2.collection.OAuth2AccessToken;
import com.pamarin.oauth2.exception.InvalidTokenException;
import com.pamarin.oauth2.service.AccessTokenVerification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepository;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
@Service
public class AccessTokenVerificationImpl implements AccessTokenVerification {

    private final OAuth2AccessTokenRepository repository;

    private final HashBasedToken hashBasedToken;

    @Autowired
    public AccessTokenVerificationImpl(OAuth2AccessTokenRepository repository, HashBasedToken hashBasedToken) {
        this.repository = repository;
        this.hashBasedToken = hashBasedToken;
    }

    @Override
    public OAuth2AccessToken verify(String accessToken) {
        OAuth2AccessToken output = OAuth2AccessToken.builder().build();
        if (!hashBasedToken.matches(accessToken, new UserDetailsServiceImpl(repository, output))) {
            throw new InvalidTokenException("Invalid access token.");
        }
        return output;
    }

    public static class UserDetailsServiceImpl implements UserDetailsService {

        private final OAuth2AccessTokenRepository repository;

        private final OAuth2AccessToken output;

        public UserDetailsServiceImpl(OAuth2AccessTokenRepository repository, OAuth2AccessToken output) {
            this.repository = repository;
            this.output = output;
        }

        @Override
        public UserDetails loadUserByUsername(String tokenId) {
            OAuth2AccessToken token = repository.findByTokenId(tokenId);
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
