/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.oauth2.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.view.ModelAndViewBuilder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author jitta
 */
@Controller
public class SimpleCtrl {

    private static final String CLIENT_ID = "b98e21b4-ce2a-11e7-abc4-cec278b6b50a";

    @Value("${oauth2.server.hostUrl}")
    private String oauth2HostUrl;

    @Autowired
    private HostUrlProvider hostUrlProvider;

    @Autowired
    private RestTemplate restTemplate;

    private String getAuthorizeUrl() throws UnsupportedEncodingException {
        return "{oauth2_host}/authorize?response_type=code&client_id={client_id}&redirect_uri={redirect_uri}&scope={scope}&state={state}"
                .replace("{oauth2_host}", oauth2HostUrl)
                .replace("{client_id}", CLIENT_ID)
                .replace("{redirect_uri}", URLEncoder.encode(hostUrlProvider.provide() + "/code", "utf-8"))
                .replace("{scope}", "user:public_profile")
                .replace("{state}", "xyz");
    }

    @GetMapping({"", "/"})
    public ModelAndView home() {
        return new ModelAndViewBuilder()
                .setName("index")
                .addAttribute("serverDomain", oauth2HostUrl)
                .build();
    }

    @GetMapping("/authorize")
    public void authorize(HttpServletResponse response) throws UnsupportedEncodingException, IOException {
        response.sendRedirect(getAuthorizeUrl());
    }

    @GetMapping("/code")
    public ModelAndView getToken(@RequestParam("code") String code) throws UnsupportedEncodingException, JsonProcessingException {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Authorization", "Basic Yjk4ZTIxYjQtY2UyYS0xMWU3LWFiYzQtY2VjMjc4YjZiNTBhOnBhc3N3b3Jk");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", URLEncoder.encode(hostUrlProvider.provide() + "/code", "utf-8"));
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(oauth2HostUrl + "/token", request, Map.class);

        return new ModelAndViewBuilder()
                .setName("code")
                .addAttribute("code", code)
                .addAttribute("response", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response.getBody()))
                .build();
    }

}
