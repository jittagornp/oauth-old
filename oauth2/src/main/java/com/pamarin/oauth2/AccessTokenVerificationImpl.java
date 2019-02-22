/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.HashBasedToken;
import com.pamarin.oauth2.domain.OAuth2AccessToken;
import com.pamarin.oauth2.exception.InvalidTokenException;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepo;
import com.pamarin.oauth2.service.AccessTokenVerification;
import org.springframework.beans.factory.annotation.Autowired;
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

    private UserDetailsService userDetailsService(Output output) {
        return id -> {
            OAuth2AccessToken token = accessTokenRepo.findById(id);
            if (token == null) {
                throw new UsernameNotFoundException("Not found access token");
            }

            output.setId(token.getId());
            output.setIssuedAt(token.getIssuedAt());
            output.setExpiresAt(token.getExpiresAt());
            output.setUserId(token.getUserId());
            output.setClientId(token.getClientId());
            output.setSessionId(token.getSessionId());
            return DefaultUserDetails.builder()
                    .username(token.getId())
                    .password(token.getSecretKey())
                    .build();
        };
    }

    @Override
    public Output verify(String accessToken) {
        Output output = Output.builder().build();
        if (!hashBasedToken.matches(accessToken, userDetailsService(output))) {
            throw new InvalidTokenException("Invalid access token.");
        }
        return output;
    }

}
