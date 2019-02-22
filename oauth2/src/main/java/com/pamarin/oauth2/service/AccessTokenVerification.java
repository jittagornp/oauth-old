/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
public interface AccessTokenVerification {

    Output verify(String accessToken);

    @Getter
    @Setter
    @Builder
    public static class Output {

        private String id;

        private long issuedAt;

        private long expiresAt;

        private String userId;

        private String clientId;
        
        private String sessionId;

    }
}
