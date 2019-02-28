/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.security.LoginSession;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author jitta
 */
@Controller
public class LogoutCtrl {

    @Autowired
    private LoginSession loginSession;

    @Autowired
    private HostUrlProvider hostUrlProvider;

    @GetMapping("/logout")
    public void getLogout(@RequestParam(value = "redirect_uri", required = false) String redirectUri, HttpServletResponse httpResp) throws IOException {

        loginSession.logout();

        if (!hasText(redirectUri)) {
            redirectUri = hostUrlProvider.provide();
        }

        httpResp.sendRedirect(redirectUri);

    }

    @PostMapping("/logout")
    public void postLogout() {

        loginSession.logout();

    }

}
