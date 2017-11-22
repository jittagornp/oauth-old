/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pamarin.oauth2.util.QuerystringBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * https://tools.ietf.org/html/rfc6749#section-4.1.4
 *
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/25
 */
@Getter
@Setter
@Builder
public class AccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expries_in")
    private long expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String state;

    public String buildQuerystringWithoutRefreshToken() {
        return new QuerystringBuilder()
                .addParameter("access_token", accessToken)
                .addParameter("state", state)
                .addParameter("token_type", tokenType)
                .addParameter("expires_in", String.valueOf(expiresIn))
                .build();
    }
}
