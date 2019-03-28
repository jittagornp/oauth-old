/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.collection;

import com.pamarin.commons.util.ObjectEquals;
import com.pamarin.oauth2.domain.OAuth2Token;
import java.util.Objects;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Getter
@Setter
@Builder
@Document(collection = OAuth2RefreshToken.COLLECTION_NAME)
public class OAuth2RefreshToken implements OAuth2Token {

    public static final String COLLECTION_NAME = "oauth2_refresh_token";

    @Id
    private String id;

    @Indexed
    @Field("token_id")
    private String tokenId;

    @Field("issued_at")
    private long issuedAt;

    @Field("expires_at")
    private long expiresAt;

    @Field("user_id")
    private String userId;

    @Field("client_id")
    private String clientId;

    @Field("expire_minutes")
    private int expireMinutes;

    @Field("secret_key")
    private String secretKey;

    @Field("session_id")
    private String sessionId;

    public OAuth2RefreshToken() {
    }

    public OAuth2RefreshToken(String id, String tokenId, long issuedAt, long expiresAt, String userId, String clientId, int expireMinutes, String secretKey, String sessionId) {
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
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return ObjectEquals.of(this)
                .equals(obj, (origin, other) -> {
                    return Objects.equals(origin.getId(), other.getId())
                            || Objects.equals(origin.getTokenId(), other.getTokenId());
                });
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
