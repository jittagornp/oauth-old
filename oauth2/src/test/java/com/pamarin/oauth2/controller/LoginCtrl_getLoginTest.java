/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.oauth2.IntegrationTestBase;
import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.security.hashing.Hashing;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

    @MockBean
    private Hashing hashing;
    
    @Before
    public void before() {
        when(hostUrlProvider.provide()).thenReturn("http://localhost");
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenResponseTypeIsAAA() throws Exception {
        this.mockMvc.perform(get("/login?response_type=AAA"))
                .andExpect(status().isBadRequest())
                .andExpect((MvcResult mr) -> {
                    String string = mr.getResponse().getContentAsString();
                    assertThat(string).contains("{&quot;error&quot;:&quot;invalid_request&quot;,&quot;error_status&quot;:400,&quot;error_description&quot;:&quot;Require parameter client_id (String)&quot;}");
                });
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenClientIdIs000000AndRedirectUriIsEmpty() throws Exception {
        this.mockMvc.perform(get("/login?response_type=code&client_id=000000"))
                .andExpect(status().isBadRequest())
                .andExpect((MvcResult mr) -> {
                    String string = mr.getResponse().getContentAsString();
                    assertThat(string).contains("{&quot;error&quot;:&quot;invalid_request&quot;,&quot;error_status&quot;:400,&quot;error_description&quot;:&quot;Require parameter redirect_uri (String)&quot;}");
                });
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenEmptyClientIdAndRedirectUriIsAAA() throws Exception {
        this.mockMvc.perform(get("/login?response_type=code&redirect_uri=AAA"))
                .andExpect(status().isBadRequest())
                .andExpect((MvcResult mr) -> {
                    String string = mr.getResponse().getContentAsString();
                    assertThat(string).contains("{&quot;error&quot;:&quot;invalid_request&quot;,&quot;error_status&quot;:400,&quot;error_description&quot;:&quot;Require parameter client_id (String)&quot;}");
                });
    }

    @Test
    public void shouldBeErrorUnsupportResponseType_whenInvalidRedirectUri() throws Exception {
        this.mockMvc.perform(get("/login?response_type=AAA&client_id=000000&redirect_uri=http://localhost/callback"))
                .andExpect(status().isBadRequest())
                .andExpect((MvcResult mr) -> {
                    String string = mr.getResponse().getContentAsString();
                    assertThat(string).contains("{&quot;error&quot;:&quot;unsupported_response_type&quot;,&quot;error_status&quot;:400}");
                });
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenInvalidRedirectUri() throws Exception {
        this.mockMvc.perform(get("/login?response_type=code&client_id=000000&redirect_uri=AAA"))
                .andExpect(status().isBadRequest())
                .andExpect((MvcResult mr) -> {
                    String string = mr.getResponse().getContentAsString();
                    assertThat(string).contains("{&quot;error&quot;:&quot;invalid_request&quot;,&quot;error_status&quot;:400,&quot;error_description&quot;:&quot;Invalid format &#39;AAA&#39;&quot;}");
                });
    }

    @Test
    public void shouldBeErrorInvalidScope_whenInvalidScope() throws Exception {
        this.mockMvc.perform(get("/login?response_type=code&client_id=000000&redirect_uri=http://localhost/callback&scope=write"))
                .andExpect(status().isBadRequest())
                .andExpect((MvcResult mr) -> {
                    String string = mr.getResponse().getContentAsString();
                    assertThat(string).contains("{&quot;error&quot;:&quot;invalid_scope&quot;,&quot;error_status&quot;:400}");
                });
    }
    
    @Test
    public void shouldBeErrorInvalidRequest_whenSignatureIsNull() throws Exception {
        this.mockMvc.perform(get("/login?response_type=code&client_id=000000&redirect_uri=http://localhost/callback&scope=read"))
                .andExpect(status().isBadRequest())
                .andExpect((MvcResult mr) -> {
                    String string = mr.getResponse().getContentAsString();
                    assertThat(string).contains("{&quot;error&quot;:&quot;invalid_request&quot;,&quot;error_status&quot;:400,&quot;error_description&quot;:&quot;Require parameter signature (String)&quot;}");
                });
    }
    
    @Test
    public void shouldBeErrorInvalidSignature_whenSignatureIsAAAAA() throws Exception {
        this.mockMvc.perform(get("/login?response_type=code&client_id=000000&redirect_uri=http://localhost/callback&scope=read&signature=AAAAA"))
                .andExpect(status().isForbidden())
                .andExpect((MvcResult mr) -> {
                    String string = mr.getResponse().getContentAsString();
                    assertThat(string).contains("{&quot;error&quot;:&quot;invalid_signature&quot;,&quot;error_status&quot;:403,&quot;error_description&quot;:&quot;Invalid signature \\&quot;AAAAA\\&quot;.&quot;}");
                });
    }

    @Test
    public void shouldBeOk_whenScopeIsRead() throws Exception {
        when(hashing.matches(any(byte[].class), any(String.class))).thenReturn(true);
        this.mockMvc.perform(get("/login?response_type=code&client_id=000000&redirect_uri=http://localhost/callback&scope=read&signature=BBBBB"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("processUrl", "http://localhost/login?response_type=code&client_id=000000&redirect_uri=http%3A%2F%2Flocalhost%2Fcallback&scope=read"));
    }
}
