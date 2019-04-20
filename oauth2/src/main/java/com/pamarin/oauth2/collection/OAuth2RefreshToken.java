/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.collection;

import com.pamarin.commons.util.ObjectEquals;
import com.pamarin.oauth2.model.OAuth2Token;
import java.util.Objects;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = OAuth2RefreshToken.COLLECTION_NAME)
public class OAuth2RefreshToken implements OAuth2Token {

    public static final String COLLECTION_NAME = "oauth2_refresh_token";

    @Id
    private String id;

    @Indexed
    private String tokenId;

    private long issuedAt;

    private long expiresAt;

    private String userId;

    private String clientId;

    private int expireMinutes;

    private String secretKey;

    private String sessionId;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return ObjectEquals.of(this)
                .equals(obj, (origin, other) -> Objects.equals(origin.getId(), other.getId())
                || Objects.equals(origin.getTokenId(), other.getTokenId())
                );
    }

}
