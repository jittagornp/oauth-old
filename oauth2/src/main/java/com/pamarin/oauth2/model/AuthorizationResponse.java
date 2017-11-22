/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.model;

import com.pamarin.oauth2.util.QuerystringBuilder;
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
public class AuthorizationResponse {

    private String code;

    private String state;

    public String buildQuerystring() {
        return new QuerystringBuilder()
                .addParameter("code", code)
                .addParameter("state", state)
                .build();
    }
}
