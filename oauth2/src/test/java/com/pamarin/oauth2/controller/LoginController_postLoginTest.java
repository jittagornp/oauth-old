/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.oauth2.IntegrationTestBase;
import com.pamarin.oauth2.exception.InvalidUsernamePasswordException;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.doThrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.pamarin.oauth2.service.LoginService;
import javax.servlet.http.Cookie;
import org.springframework.http.HttpHeaders;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/21
 */
@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
public class LoginController_postLoginTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
        return headers;
    }

    private Cookie[] cookies() {
        return new Cookie[]{
            new Cookie("user-agent", "xyz"),
            new Cookie("user-session", "xyz")
        };
    }

    @Test
    public void shouldBeInvalidRequest_whenUsernameAndPasswordIsEmpty() throws Exception {
        doThrow(InvalidUsernamePasswordException.class)
                .when(loginService)
                .login(null, null);
        this.mockMvc.perform(
                post("/login?response_type=code&client_id=000000&redirect_uri=http://localhost/callback&scope=read")
                        .servletPath("/login")
                        .headers(headers())
                        .cookie(cookies())
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost:-1/login?error=invalid_username_password&response_type=code&client_id=000000&redirect_uri=http%3A%2F%2Flocalhost%2Fcallback&scope=read"));
    }

    @Test
    public void shouldBeInvalidUsernamePassword_whenUsernameAndPasswordIsAAA() throws Exception {
        doThrow(InvalidUsernamePasswordException.class)
                .when(loginService)
                .login("AAA", "AAA");
        this.mockMvc.perform(
                post("/login?response_type=code&client_id=000000&redirect_uri=http://localhost/callback&scope=read")
                        .servletPath("/login")
                        .headers(headers())
                        .cookie(cookies())
                        .param("username", "AAA")
                        .param("password", "AAA")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost:-1/login?error=invalid_username_password&response_type=code&client_id=000000&redirect_uri=http%3A%2F%2Flocalhost%2Fcallback&scope=read"));
    }

    @Test
    public void shouldBeOk() throws Exception {
        this.mockMvc.perform(
                post("/login?response_type=code&client_id=000000&redirect_uri=http://localhost/callback&scope=read")
                        .servletPath("/login")
                        .headers(headers())
                        .cookie(cookies())
                        .param("username", "test")
                        .param("password", "password")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost:-1/authorize?response_type=code&client_id=000000&redirect_uri=http%3A%2F%2Flocalhost%2Fcallback&scope=read"));
    }
}
