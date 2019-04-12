/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.service.ClientVerification;
import com.pamarin.oauth2.service.LogoutService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jitta
 */
@Controller
public class LogoutEnpointController {

    @Autowired
    private LogoutService logoutService;

    @Autowired
    private ClientVerification clientVerification;

    @GetMapping("/logout")
    public void getLogout(
            @RequestParam("client_id") String clientId,
            @RequestParam("redirect_uri") String redirectUri,
            HttpServletResponse httpResp
    ) throws IOException {

        clientVerification.verifyClientIdAndRedirectUri(clientId, redirectUri);

        logoutService.logout();

        httpResp.sendRedirect(redirectUri);

    }

    //request from backend by access_token in header Authorization : bearer xxx
    @ResponseBody
    @PostMapping("/logout")
    public void postLogout(@RequestHeader("Authorization") String authrorization) {

        logoutService.logout();

    }
}
