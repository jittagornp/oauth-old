/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.oauth2.IntegrationTestBase;
import com.pamarin.oauth2.exception.InvalidClientIdException;
import com.pamarin.oauth2.exception.InvalidRedirectUriException;
import com.pamarin.oauth2.exception.InvalidResponseTypeException;
import com.pamarin.oauth2.exception.InvalidScopeException;
import com.pamarin.oauth2.exception.RequireApprovalException;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.service.AuthorizationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/25
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AuthorizeEndpointCtrl.class)
public class AuthorizeEndpointCtrl_authorizeTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorizationService authorizationService;

    @Test
    public void shouldBeErrorInvalidRequest_whenEmptyParameter() throws Exception {
        this.mockMvc.perform(get("/authorize"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid_request"));
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenInvalidParameter1() throws Exception {
        this.mockMvc.perform(get("/authorize?response_type=code"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid_request"));
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenInvalidParameter2() throws Exception {
        this.mockMvc.perform(get("/authorize?response_type=code&client_id=123456"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid_request"));
    }

    @Test
    public void shouldBeErrorUnsupportedResponseType_whenResponseTypeIsAAA() throws Exception {
        when(authorizationService.authorize(any(AuthorizationRequest.class)))
                .thenThrow(InvalidResponseTypeException.class);
        this.mockMvc.perform(get("/authorize?response_type=AAA&client_id=123456&redirect_uri=http://localhost/callback"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/callback?error=unsupported_response_type"));
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenRedirectUriIsCallback() throws Exception {
        when(authorizationService.authorize(any(AuthorizationRequest.class)))
                .thenThrow(new InvalidRedirectUriException("/callback", "Invalid redirect_uri."));
        this.mockMvc.perform(get("/authorize?response_type=AAA&client_id=123456&redirect_uri=/callback"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid_request"));
    }

    @Test
    public void shouldBeRedirect2Login_whenNotLogin() throws Exception {
        when(authorizationService.authorize(any(AuthorizationRequest.class))).thenReturn("/login?response_type=code&client_id=123456&redirect_uri=http://localhost/callback");
        this.mockMvc.perform(get("/authorize?response_type=code&client_id=123456&redirect_uri=http://localhost/callback"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?response_type=code&client_id=123456&redirect_uri=http://localhost/callback"));
    }

    @Test
    public void shouldBeErrorInvalidClient_whenThrowInvalidClientIdException() throws Exception {
        when(authorizationService.authorize(any(AuthorizationRequest.class)))
                .thenThrow(InvalidClientIdException.class);
        this.mockMvc.perform(get("/authorize?response_type=code&client_id=123456&redirect_uri=http://localhost/callback"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/callback?error=invalid_client"));
    }

    @Test
    public void shouldBeErrorInvalidScope_whenThrowInvalidScopeException() throws Exception {
        when(authorizationService.authorize(any(AuthorizationRequest.class)))
                .thenThrow(InvalidScopeException.class);
        this.mockMvc.perform(get("/authorize?response_type=code&client_id=123456&redirect_uri=http://localhost/callback&scope=AAA"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/callback?error=invalid_scope"));
    }

    @Test
    public void shouldBeErrorServerError_whenThrowException() throws Exception {
        when(authorizationService.authorize(any(AuthorizationRequest.class)))
                .thenThrow(Exception.class);
        this.mockMvc.perform(get("/authorize?response_type=code&client_id=123456&redirect_uri=http://localhost/callback"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/callback?error=server_error"));
    }

    @Test
    public void shouldBeReturnViewAuthorize_whenThrowRequireApprovalException() throws Exception {
        when(authorizationService.authorize(any(AuthorizationRequest.class)))
                .thenThrow(RequireApprovalException.class);
        this.mockMvc.perform(get("/authorize?response_type=code&client_id=123456&redirect_uri=http://localhost/callback"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorize"));
    }
}
