/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jitta
 */
@Getter
@Setter
@Builder
public class OAuth2AuthorizationCode implements OAuth2Token {

    private String id;

    private long issuedAt;

    private long expiresAt;

    private String userId;

    private String clientId;

    private int expireMinutes;

    private String secretKey;

    private String sessionId;

    private List<String> scopes;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public List<String> getScopes() {
        if (scopes == null) {
            scopes = new ArrayList<>();
        }
        return scopes;
    }
}
