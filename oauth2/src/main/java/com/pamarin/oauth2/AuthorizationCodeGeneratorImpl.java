/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.model.AuthorizationResponse;
import com.pamarin.oauth2.service.AuthorizationCodeGenerator;
import com.pamarin.commons.security.LoginSession;
import static com.pamarin.commons.util.DateConverterUtils.convert2Date;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.pamarin.commons.security.RSAKeyPairs;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Component
public class AuthorizationCodeGeneratorImpl implements AuthorizationCodeGenerator {

    private static final int EXPIRES_MINUTE = 1;

    @Autowired
    private LoginSession loginSession;

    @Autowired
    @Qualifier("autorizationCodeKeyPairs")
    private RSAKeyPairs keyPairs;

    private Algorithm getAlgorithm() {
        return Algorithm.RSA256(keyPairs.getPublicKey(), keyPairs.getPrivateKey());
    }

    @Override
    public AuthorizationResponse generate(AuthorizationRequest req) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expires = now.plusMinutes(EXPIRES_MINUTE);
        String[] arr = new String[req.getScopes().size()];
        UserDetails userDetails = loginSession.getUserDetails();
        String code = JWT.create()
                .withSubject(req.getClientId())
                .withIssuer(userDetails.getUsername())
                .withIssuedAt(convert2Date(now))
                .withExpiresAt(convert2Date(expires))
                .withArrayClaim("scopes", req.getScopes().toArray(arr))
                .withClaim("session", loginSession.getSessionId())
                .sign(getAlgorithm());
        return AuthorizationResponse.builder()
                .code(code)
                .state(req.getState())
                .build();
    }
}
