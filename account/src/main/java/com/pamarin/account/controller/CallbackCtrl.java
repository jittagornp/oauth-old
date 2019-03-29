/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.account.controller;

import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.oauth2.client.sdk.OAuth2AccessToken;
import com.pamarin.oauth2.client.sdk.OAuth2Client;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jitta
 */
@Controller
public class CallbackCtrl {

    @Autowired
    private HostUrlProvider hostUrlProvider;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${oauth2.authorization-server.hostUrl}")
    private String authorizationServerHostUrl;

    @Autowired
    private OAuth2Client oauth2Client;

    @GetMapping("/callback")
    public void callback(@RequestParam("code") String code, HttpServletResponse httpRep) throws IOException {
        OAuth2AccessToken accessToken = oauth2Client.getAccessTokenByAuthorizationCode(code);
        Cookie accessTokenCookie = buildCookie("access_token", accessToken.getAccessToken(), 60 * 60 * 24); //24 hours
        Cookie refreshTokenCookie = buildCookie("refresh_token", accessToken.getAccessToken(), 60 * 60 * 24 * 14); //14 days
        httpRep.addCookie(accessTokenCookie);
        httpRep.addCookie(refreshTokenCookie);
        httpRep.sendRedirect("/");
    }

    private Cookie buildCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge); //24 hours
        cookie.setSecure(hostUrlProvider.provide().startsWith("https://"));
        return cookie;
    }

    private ResponseEntity<Map> getSession(String accessToken) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Authorization", "Bearer " + accessToken);

        //MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);

        return restTemplate.postForEntity(authorizationServerHostUrl + "/session", request, Map.class);
    }
}
