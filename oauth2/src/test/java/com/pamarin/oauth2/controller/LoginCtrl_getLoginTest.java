/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.oauth2.IntegrationTestBase;
import com.pamarin.oauth2.provider.HostUrlProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/11
 */
@RunWith(SpringRunner.class)
@WebMvcTest(LoginCtrl.class)
public class LoginCtrl_getLoginTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HostUrlProvider hostUrlProvider;

    @Before
    public void before() {
        when(hostUrlProvider.provide()).thenReturn("http://localhost");
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenResponseTypeIsAAA() throws Exception {
        this.mockMvc.perform(get("/login?response_type=AAA"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid_request"));
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenClientIdIs000000AndRedirectUriIsEmpty() throws Exception {
        this.mockMvc.perform(get("/login?response_type=code&client_id=000000"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid_request"));
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenEmptyClientIdAndRedirectUriIsAAA() throws Exception {
        this.mockMvc.perform(get("/login?response_type=code&redirect_uri=AAA"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid_request"));
    }

    @Test
    public void shouldBeErrorUnsupportResponseType_whenInvalidRedirectUri() throws Exception {
        this.mockMvc.perform(get("/login?response_type=AAA&client_id=000000&redirect_uri=http://localhost/callback"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/callback?error=unsupported_response_type"));
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenInvalidRedirectUri() throws Exception {
        this.mockMvc.perform(get("/login?response_type=code&client_id=000000&redirect_uri=AAA"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid_request"));
    }

    @Test
    public void shouldBeErrorInvalidScope_whenInvalidScope() throws Exception {
        this.mockMvc.perform(get("/login?response_type=code&client_id=000000&redirect_uri=http://localhost/callback&scope=write"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/callback?error=invalid_scope"));
    }

//    @Test
//    public void shouldBeOk_whenEmptyScope() throws Exception {
//        this.mockMvc.perform(get("/login?response_type=code&client_id=000000&redirect_uri=http://localhost/callback"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("login"))
//                .andExpect(model().attribute("processUrl", "http://localhost/login?response_type=code&client_id=000000&redirect_uri=http://localhost/callback&scope=read"));
//    }
    @Test
    public void shouldBeOk_whenScopeIsRead() throws Exception {
        this.mockMvc.perform(get("/login?response_type=code&client_id=000000&redirect_uri=http://localhost/callback&scope=read"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("processUrl", "http://localhost/login?response_type=code&client_id=000000&redirect_uri=http%3A%2F%2Flocalhost%2Fcallback&scope=read"));
    }
}
