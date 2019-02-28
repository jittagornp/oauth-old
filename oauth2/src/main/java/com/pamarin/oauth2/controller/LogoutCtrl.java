/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.service.ClientVerification;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @Autowired
    private ClientVerification clientVerification;

    @GetMapping("/logout")
    public void getLogout(
            @RequestParam("client_id") String clientId,
            @RequestParam("redirect_uri") String redirectUri,
            HttpServletResponse httpResp
    ) throws IOException {

        clientVerification.verifyClientIdAndRedirectUri(clientId, redirectUri);

        loginSession.logout();

        if (!hasText(redirectUri)) {
            redirectUri = hostUrlProvider.provide();
        }

        httpResp.sendRedirect(redirectUri);

    }

    //request from backend by access_token in header Authorization : bearer xxx
    @PostMapping("/logout")
    public void postLogout(@RequestHeader("Authorization") String authrorization) {

        loginSession.logout();

    }

}
