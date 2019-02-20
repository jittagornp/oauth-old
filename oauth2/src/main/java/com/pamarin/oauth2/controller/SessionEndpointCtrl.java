/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.commons.util.HttpAuthorizeBearerParser;
import com.pamarin.oauth2.model.OAuth2Session;
import com.pamarin.oauth2.service.OAuth2SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
    private OAuth2SessionService sessionService;

    @PostMapping("/session")
    public OAuth2Session getSession(@RequestHeader("Authorization") String authorization) {
        String accessToken = httpAuthorizeBearerParser.parse(authorization);
        return sessionService.getSession(accessToken);
    }

}
