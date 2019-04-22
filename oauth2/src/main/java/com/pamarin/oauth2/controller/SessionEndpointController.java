/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.oauth2.constant.OAuth2Constant;
import com.pamarin.oauth2.collection.OAuth2AccessToken;
import com.pamarin.oauth2.model.OAuth2Session;
import com.pamarin.oauth2.session.OAuth2SessionService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/09
 */
@RestController
public class SessionEndpointController {

    @Autowired
    private OAuth2SessionService sessionService;

    @PostMapping("/session")
    public OAuth2Session getSession(HttpServletRequest httpReq) {
        return sessionService.getSessionByOAuth2AccessToken(getAccessToken(httpReq));
    }

    private OAuth2AccessToken getAccessToken(HttpServletRequest httpReq) {
        return (OAuth2AccessToken) httpReq.getAttribute(OAuth2Constant.ACCESS_TOKEN_ATTRIBUTE);
    }
}
