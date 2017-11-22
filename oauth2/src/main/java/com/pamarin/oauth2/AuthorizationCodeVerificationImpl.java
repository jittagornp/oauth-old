/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.security.KeyPairs;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pamarin.oauth2.model.TokenBase;
import com.pamarin.oauth2.service.TokenVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Component("authorizationCodeVerification")
public class AuthorizationCodeVerificationImpl implements TokenVerification {
    
    @Autowired
    @Qualifier("autorizationCodeKeyPairs")
    private KeyPairs keyPairs;

    @Override
    public TokenBase verify(String token) {
        DecodedJWT decoded = JWT.require(Algorithm.RSA256(keyPairs.getRSAPublicKey(), null))
                .build()
                .verify(token);
        return TokenBase.builder()
                .userId(Long.valueOf(decoded.getIssuer()))
                .username(decoded.getSubject())
                .build();
    }
}
