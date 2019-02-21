/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pamarin.oauth2.model.TokenBase;
import com.pamarin.oauth2.service.TokenVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.pamarin.commons.security.RSAKeyPairs;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Component("authorizationCodeVerification")
public class AuthorizationCodeVerificationImpl implements TokenVerification {
    
    @Autowired
    @Qualifier("autorizationCodeKeyPairs")
    private RSAKeyPairs keyPairs;

    @Override
    public TokenBase verify(String token) {
        DecodedJWT decoded = JWT.require(Algorithm.RSA256(keyPairs.getPublicKey(), null))
                .build()
                .verify(token);
        return TokenBase.builder()
                .clientId(decoded.getSubject())
                .userId(decoded.getIssuer())
                .sessionId(decoded.getClaim("session").asString())
                .build();
    }
}
