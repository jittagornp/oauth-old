/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.commons.util.HttpAuthorizeBearerParser;
import com.pamarin.oauth2.model.OAuth2Session;
import com.pamarin.oauth2.service.AccessTokenVerification;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/09
 */
@RestController
public class SessionEndpointCtrl {

    @Autowired
    private HttpAuthorizeBearerParser httpAuthorizeBearerParser;

    @Autowired
    private AccessTokenVerification accessTokenVerification;

    @GetMapping("/session")
    public OAuth2Session getSession(@RequestHeader("Authorization") String authorization) {
        String accessToken = httpAuthorizeBearerParser.parse(authorization);
        AccessTokenVerification.Output output = accessTokenVerification.verify(accessToken);
        return OAuth2Session.builder()
                .id(output.getId())
                .issuedAt(output.getIssuedAt())
                .expiresAt(output.getExpiresAt())
                .user(
                        OAuth2Session.User.builder()
                                .id(output.getUserId())
                                .name("นาย สมชาย ใจดี")
                                .authorities(Arrays.asList("sso"))
                                .build()
                )
                .client(OAuth2Session.Client.builder()
                        .id(output.getClientId())
                        .name("ระบบทดสอบ OAuth2")
                        .scopes(Arrays.asList("read"))
                        .build()
                )
                .build();
    }

}
