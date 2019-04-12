/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.HashBasedToken;
import org.springframework.beans.factory.annotation.Autowired;
import com.pamarin.oauth2.domain.OAuth2AuthorizationCode;
import com.pamarin.oauth2.exception.InvalidTokenException;
import com.pamarin.oauth2.service.AuthorizationCodeVerification;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.pamarin.oauth2.repository.OAuth2AuthorizationCodeRepository;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Service
public class AuthorizationCodeVerificationImpl implements AuthorizationCodeVerification {

    @Autowired
    private OAuth2AuthorizationCodeRepository authorizationCodeRepository;

    @Autowired
    private HashBasedToken hashBasedToken;

    private UserDetailsService userDetailsService(OAuth2AuthorizationCode output) {
        return tokenId -> {
            OAuth2AuthorizationCode code = authorizationCodeRepository.findByTokenId(tokenId);
            if (code == null) {
                throw new UsernameNotFoundException("Not found authorization code.");
            }

            //revoke code
            authorizationCodeRepository.deleteByTokenId(tokenId);

            String[] ignoreProperties = new String[]{"secretKey"};
            BeanUtils.copyProperties(code, output, ignoreProperties);
            return DefaultUserDetails.builder()
                    .username(code.getTokenId())
                    .password(code.getSecretKey())
                    .build();
        };
    }

    @Override
    public OAuth2AuthorizationCode verify(String token) {
        OAuth2AuthorizationCode code = OAuth2AuthorizationCode.builder().build();
        if (!hashBasedToken.matches(token, userDetailsService(code))) {
            throw new InvalidTokenException("Invalid authorization code.");
        }
        return code;
    }
}
