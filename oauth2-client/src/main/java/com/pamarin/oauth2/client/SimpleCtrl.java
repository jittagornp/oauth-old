/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.oauth2.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.view.ModelAndViewBuilder;
import com.pamarin.oauth2.client.sdk.OAuth2AccessToken;
import com.pamarin.oauth2.client.sdk.OAuth2ClientOperations;
import com.pamarin.oauth2.client.sdk.OAuth2Session;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author jitta
 */
@Controller
public class SimpleCtrl {

    @Autowired
    private HostUrlProvider hostUrlProvider;

    @Autowired
    private OAuth2ClientOperations operations;

    private String getAuthorizeUrl() throws UnsupportedEncodingException {
        return "{oauth2_host}/authorize?response_type=code&client_id={client_id}&redirect_uri={redirect_uri}&scope={scope}&state={state}"
                .replace("{oauth2_host}", operations.getAuthorizationServerHostUrl())
                .replace("{client_id}", operations.getClientId())
                .replace("{redirect_uri}", URLEncoder.encode(hostUrlProvider.provide() + "/code", "utf-8"))
                .replace("{scope}", "user:public_profile")
                .replace("{state}", "xyz");
    }

    @GetMapping({"", "/"})
    public ModelAndView home() {
        return new ModelAndViewBuilder()
                .setName("index")
                .addAttribute("serverDomain", operations.getAuthorizationServerHostUrl())
                .build();
    }

    @GetMapping("/authorize")
    public void authorize(HttpServletResponse response) throws IOException {
        response.sendRedirect(getAuthorizeUrl());
    }

    @GetMapping("/code")
    public ModelAndView getToken(@RequestParam("code") String code) throws JsonProcessingException {

        OAuth2AccessToken accessToken = operations.getAccessTokenByAuthorizationCode(code);
        OAuth2Session session = operations.getSession(accessToken.getAccessToken());

        ObjectWriter writter = new ObjectMapper().writerWithDefaultPrettyPrinter();

        return new ModelAndViewBuilder()
                .setName("code")
                .addAttribute("code", code)
                .addAttribute("response", writter.writeValueAsString(accessToken))
                .addAttribute("loginSession", writter.writeValueAsString(session))
                .build();
    }
}
