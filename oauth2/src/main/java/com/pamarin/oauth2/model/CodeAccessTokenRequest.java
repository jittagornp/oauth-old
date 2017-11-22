/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/26
 */
@Getter
@Setter
@Builder
public class CodeAccessTokenRequest {

    private String grantType;

    private String code;

    private String redirectUri;

    private String clientId;

    private String clientSecret;

}
