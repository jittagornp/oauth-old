/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.domain;

import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
@Getter
@Setter
@Builder
@Document(collection = OAuth2AccessToken.COLLECTION_NAME)
public class OAuth2AccessToken implements OAuth2Token {

    public static final String COLLECTION_NAME = "oauth2_access_token";

    @Id
    private String id; 

    private String tokenId;

    private long issuedAt;

    private long expiresAt;

    private String userId;

    private String clientId;

    private int expireMinutes;

    private String secretKey;

    private String sessionId;

    public OAuth2AccessToken() {
    }

    public OAuth2AccessToken(String id, String tokenId, long issuedAt, long expiresAt, String userId, String clientId, int expireMinutes, String secretKey, String sessionId) {
        this.id = id;
        this.tokenId = tokenId;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.userId = userId;
        this.clientId = clientId;
        this.expireMinutes = expireMinutes;
        this.secretKey = secretKey;
        this.sessionId = sessionId;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
