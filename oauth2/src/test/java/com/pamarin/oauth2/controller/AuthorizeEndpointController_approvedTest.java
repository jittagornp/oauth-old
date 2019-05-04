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
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/23
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AuthorizeEndpointController.class)
public class AuthorizeEndpointController_approvedTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorizationService authorizationService;

    @Test
    public void shouldBeErrorInvalidRequest_whenEmptyParameter() throws Exception {
        this.mockMvc.perform(post("/authorize").servletPath("/authorize"))
                .andExpect(status().isBadRequest())
                .andExpect((MvcResult mr) -> {
                    String string = mr.getResponse().getContentAsString();
                    assertThat(string).contains("{&quot;error&quot;:&quot;invalid_request&quot;,&quot;error_status&quot;:400,&quot;error_description&quot;:&quot;Require parameter &#39;answer=approved or answer=not_approve&#39;&quot;}");
                });
    }

    @Test
    public void shouldBeErrorInvalidRequest_whenInvalidAnswer() throws Exception {
        this.mockMvc.perform(post("/authorize?response_type=code&client_id=123456&redirect_uri=http://localhost/callback").servletPath("/authorize"))
                .andExpect(status().isBadRequest())
                .andExpect((MvcResult mr) -> {
                    String string = mr.getResponse().getContentAsString();
                    assertThat(string).contains("{&quot;error&quot;:&quot;invalid_request&quot;,&quot;error_status&quot;:400,&quot;error_description&quot;:&quot;Require parameter &#39;answer=approved or answer=not_approve&#39;&quot;}");
                });
    }

    @Test
    public void shouldBeRedirectToAuthoriza_whenAnswerIsApproved() throws Exception {
        String returnValue = "http://localhost/authorize?response_type=code&client_id=123456&redirect_uri=http%3A%2F%2Flocalhost/callback";
        when(authorizationService.approved(any(AuthorizationRequest.class)))
                .thenReturn(returnValue);
        this.mockMvc.perform(post("/authorize?response_type=code&client_id=123456&redirect_uri=http://localhost/callback&answer=approved").servletPath("/authorize"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(returnValue));
    }
}
