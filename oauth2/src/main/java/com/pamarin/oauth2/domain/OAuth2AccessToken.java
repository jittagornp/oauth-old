/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
@Getter
@Setter
@Builder
public class OAuth2AccessToken implements OAuth2Token {

    private String id;

    private long issuedAt;

    private long expiresAt;

    private String userId;

    private String clientId;
    
    private int expireMinutes; 
    
    private String secretKey;
    
    private String sessionId;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
