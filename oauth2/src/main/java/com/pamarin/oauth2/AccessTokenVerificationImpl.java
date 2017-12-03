/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.service.AccessTokenVerification;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
@Service
public class AccessTokenVerificationImpl implements AccessTokenVerification {

    @Override
    public Output verify(String accessToken) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expires = now.plusMinutes(30);
        return Output.builder()
                .id(UUID.randomUUID().toString())
                .issuedAt(Timestamp.valueOf(now).getTime())
                .expiresAt(Timestamp.valueOf(expires).getTime())
                .userId(UUID.randomUUID().toString())
                .clientId(UUID.randomUUID().toString())
                .build();
    }

}
