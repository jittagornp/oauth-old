/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.oauth2.IntegrationTestBase;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.commons.security.PasswordEncryption;
import com.pamarin.oauth2.service.AuthorizationCodeGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/08
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TokenEndpointCtrl.class)
public class TokenEndpointCtrlTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorizationCodeGenerator authorizationCodeGenerator;

    @MockBean
    private PasswordEncryption passwordEncryption;

    @Before
    public void mcokPasswordEncryption() {
        when(passwordEncryption.matches(any(String.class), any(String.class)))
                .thenReturn(true);
    }

    private String generateAuthorizationCode() {
        return authorizationCodeGenerator.generate(AuthorizationRequest.builder()
                .clientId("123456")
                .redirectUri("http://localhost/callback")
                .responseType("authorization_code")
                .scope("read")
                .build())
                .getCode();
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenExchangeByHttpGet() throws Exception {
        this.mockMvc.perform(get("/token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"invalid_request\",\"error_description\":\"Not support http 'GET'\"}"));
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenNotSetMediaType() throws Exception {
        this.mockMvc.perform(post("/token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"invalid_request\",\"error_description\":\"Not support media type 'null'\"}"));
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenEmptyGrantTypeParameter() throws Exception {
        this.mockMvc.perform(post("/token").contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"invalid_grant\",\"error_description\":\"Require parameter 'grant_type=authorization_code or grant_type=refresh_token'\"}"));
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenEmptyAuthorizationHeader() throws Exception {
        this.mockMvc.perform(
                post("/token?grant_type=authorization_code")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"invalid_request\",\"error_description\":\"Require header 'Authorization' as http basic\"}"));
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenEmptyCodeParameter() throws Exception {
        this.mockMvc.perform(
                post("/token?grant_type=authorization_code")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(httpBasic("test", "password"))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"invalid_request\",\"error_description\":\"Require parameter code (String)\"}"));
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenHasCodeButEmptyRedirectUriParameter() throws Exception {
        this.mockMvc.perform(
                post("/token?grant_type=authorization_code&code=XXX")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(httpBasic("test", "password"))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"invalid_request\",\"error_description\":\"Require parameter redirect_uri (String)\"}"));
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenEmptyRefreshTokenParameter() throws Exception {
        this.mockMvc.perform(
                post("/token?grant_type=refresh_token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(httpBasic("test", "password"))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"invalid_request\",\"error_description\":\"Require parameter refresh_token (String)\"}"));
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenHasRefreshTokenButEmptyRedirectUriParameter() throws Exception {
        this.mockMvc.perform(
                post("/token?grant_type=refresh_token&refresh_token=XXX")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(httpBasic("test", "password"))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"invalid_request\",\"error_description\":\"Require parameter redirect_uri (String)\"}"));
    }

    @Test
    public void shouldBeOk_whenGrantTypeIsAuthorizationCode() throws Exception {
        String code = generateAuthorizationCode();
        this.mockMvc.perform(
                post("/token?grant_type=authorization_code&code=" + code + "&redirect_uri=http://localhost/callback")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(httpBasic("test", "password"))
        )
                .andExpect(status().isOk());
    }

    @Test
    public void shouldBeOk_whenGrantTypeIsRefreshToken() throws Exception {
        this.mockMvc.perform(
                post("/token?grant_type=refresh_token&refresh_token=XXX&redirect_uri=http://localhost/callback")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(httpBasic("test", "password"))
        )
                .andExpect(status().isOk());
    }
}
