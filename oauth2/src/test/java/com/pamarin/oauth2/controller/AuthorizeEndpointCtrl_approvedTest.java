/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.oauth2.IntegrationTestBase;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/23
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AuthorizeEndpointCtrl.class)
public class AuthorizeEndpointCtrl_approvedTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorizationService authorizationService;

    @Test
    public void shouldBeErrorInvalidRequest_whenEmptyParameter() throws Exception {
        this.mockMvc.perform(post("/authorize"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid_request"));
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenInvalidAnswer() throws Exception {
        this.mockMvc.perform(post("/authorize?response_type=code&client_id=123456&redirect_uri=http://localhost/callback"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/callback?error=invalid_request&error_description=Require+parameter+%27answer%3Dapproved+or+answer%3Dnot_approve%27"));
    }

    @Test
    public void shouldBeRedirectToAuthoriza_whenAnswerIsApproved() throws Exception {
        String returnValue = "http://localhost/authorize?response_type=code&client_id=123456&redirect_uri=http%3A%2F%2Flocalhost/callback";
        when(authorizationService.approved(any(AuthorizationRequest.class)))
                .thenReturn(returnValue);
        this.mockMvc.perform(post("/authorize?response_type=code&client_id=123456&redirect_uri=http://localhost/callback&answer=approved"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(returnValue));
    }
}
