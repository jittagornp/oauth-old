/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.oauth2.model.AccessTokenResponse;
import com.pamarin.oauth2.model.CodeAccessTokenRequest;
import com.pamarin.oauth2.model.RefreshAccessTokenRequest;
import com.pamarin.oauth2.service.AccessTokenGenerator;
import com.pamarin.oauth2.util.HttpBasicAuthenParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/25
 */
@Controller
public class TokenEndpointCtrl {

    @Autowired
    private HttpBasicAuthenParser httpBasicAuthenParser;

    @Autowired
    private AccessTokenGenerator accessTokenGenerator;

    @ResponseBody
    @PostMapping(
            value = "/token",
            params = "grant_type=authorization_code",
            consumes = "application/x-www-form-urlencoded",
            produces = "application/json"
    )
    public AccessTokenResponse getTokenByAuthorizationCode(
            @RequestHeader("Authorization") String authorization,
            @RequestParam("grant_type") String grantType,
            @RequestParam("code") String code,
            @RequestParam("redirect_uri") String redirectUri
    ) throws MissingServletRequestParameterException {
        HttpBasicAuthenParser.Output basicAuthen = httpBasicAuthenParser.parse(authorization);
        return accessTokenGenerator.generate(CodeAccessTokenRequest.builder()
                .clientId(basicAuthen.getUsername())
                .clientSecret(basicAuthen.getPassword())
                .code(code)
                .grantType(grantType)
                .redirectUri(redirectUri)
                .build());
    }

    @ResponseBody
    @PostMapping(
            value = "/token",
            params = "grant_type=refresh_token",
            consumes = "application/x-www-form-urlencoded",
            produces = "application/json"
    )
    public AccessTokenResponse getTokenByRefreshToken(
            @RequestHeader("Authorization") String authorization,
            @RequestParam("grant_type") String grantType,
            @RequestParam("refresh_token") String refreshToken,
            @RequestParam("redirect_uri") String redirectUri
    ) throws MissingServletRequestParameterException {
        HttpBasicAuthenParser.Output basicAuthen = httpBasicAuthenParser.parse(authorization);
        return accessTokenGenerator.generate(RefreshAccessTokenRequest.builder()
                .clientId(basicAuthen.getUsername())
                .clientSecret(basicAuthen.getPassword())
                .refreshToken(refreshToken)
                .grantType(grantType)
                .redirectUri(redirectUri)
                .build());
    }

}
