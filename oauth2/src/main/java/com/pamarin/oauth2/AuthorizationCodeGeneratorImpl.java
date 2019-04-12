/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.HashBasedToken;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.model.AuthorizationResponse;
import com.pamarin.oauth2.service.AuthorizationCodeGenerator;
import com.pamarin.commons.security.LoginSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.pamarin.oauth2.domain.OAuth2AuthorizationCode;
import static com.pamarin.commons.util.DateConverterUtils.convert2LocalDateTime;
import java.util.Date;
import com.pamarin.oauth2.repository.OAuth2AuthorizationCodeRepository;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Component
public class AuthorizationCodeGeneratorImpl implements AuthorizationCodeGenerator {

    @Autowired
    private LoginSession loginSession;

    @Autowired
    private OAuth2AuthorizationCodeRepository authorizationCodeRepository;

    @Autowired
    private HashBasedToken hashBasedToken;

    @Override
    public AuthorizationResponse generate(AuthorizationRequest req) {
        OAuth2AuthorizationCode code = authorizationCodeRepository.save(OAuth2AuthorizationCode.builder()
                .userId(loginSession.getUserDetails().getUsername())
                .clientId(req.getClientId())
                .sessionId(loginSession.getSessionId())
                .build()
        );

        String token = hashBasedToken.hash(
                DefaultUserDetails.builder()
                        .username(code.getTokenId())
                        .password(code.getSecretKey())
                        .build(),
                convert2LocalDateTime(new Date(code.getExpiresAt()))
        );

        return AuthorizationResponse.builder()
                .code(token)
                .state(req.getState())
                .build();
    }
}
