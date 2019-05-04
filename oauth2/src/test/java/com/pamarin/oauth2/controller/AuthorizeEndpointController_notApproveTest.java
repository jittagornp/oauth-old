/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.oauth2.IntegrationTestBase;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.ratelimit.AuthorizeRateLimitService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/23
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AuthorizeEndpointController.class)
public class AuthorizeEndpointController_notApproveTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorizationService authorizationService;

    @MockBean
    private AuthorizeRateLimitService rateLimitService;

    @Test
    public void shouldBeErrorNotApprove() throws Exception {
        String returnValue = "http://localhost/callback?error=not_approve&state=XYZ";
        when(authorizationService.notApprove(any(AuthorizationRequest.class)))
                .thenReturn(returnValue);
        this.mockMvc.perform(post("/authorize?response_type=code&client_id=123456&redirect_uri=http://localhost/callback&answer=not_approve"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(returnValue));
    }
}
