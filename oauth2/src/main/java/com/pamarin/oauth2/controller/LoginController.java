/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.oauth2.converter.HttpServletRequest2AuthorizationRequestConverter;
import com.pamarin.oauth2.exception.InvalidUsernamePasswordException;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.model.LoginCredential;
import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.security.GetCsrfToken;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.service.AuthorizationRequestVerification;
import com.pamarin.commons.view.ModelAndViewBuilder;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import com.pamarin.oauth2.service.LoginService;
import com.pamarin.commons.security.hashing.StringSignature;
import com.pamarin.oauth2.exception.LockUserException;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/24
 */
@Controller
public class LoginController {

    @Autowired
    private HttpServletRequest2AuthorizationRequestConverter requestConverter;

    @Autowired
    private HostUrlProvider hostUrlProvider;

    @Autowired
    private AuthorizationRequestVerification requestVerification;

    @Autowired
    private LoginService loginService;

    @Autowired
    private LoginSession loginSession;

    @Autowired
    private StringSignature stringSignature;

    private AuthorizationRequest buildAuthorizationRequest(HttpServletRequest httpReq) throws MissingServletRequestParameterException {
        AuthorizationRequest req = requestConverter.convert(httpReq);
        req.requireParameters();
        requestVerification.verify(req);
        return req;
    }

    private void verifySignature(String querystring, String signature) throws MissingServletRequestParameterException {
        try {
            stringSignature.verify(querystring, signature);
        } catch (IllegalArgumentException ex) {
            throw new MissingServletRequestParameterException("signature", "String");
        }
    }

    @GetCsrfToken
    @GetMapping("/login")
    public ModelAndView login(HttpServletRequest httpReq, HttpServletResponse httpResp) throws MissingServletRequestParameterException, IOException {
        AuthorizationRequest req = buildAuthorizationRequest(httpReq);
        String querystring = req.buildQuerystring();
        verifySignature(querystring, httpReq.getParameter("signature"));
        if (loginSession.wasCreated()) {
            httpResp.sendRedirect(hostUrlProvider.provide() + "/authorize?" + querystring);
        }
        return new ModelAndViewBuilder()
                .setName("login")
                .addAttribute("error", httpReq.getParameter("error"))
                .addAttribute("processUrl", hostUrlProvider.provide() + "/login?" + querystring + "&signature=" + httpReq.getParameter("signature"))
                .build();
    }

    @PostMapping("/login")
    public void login(
            HttpServletRequest httpReq,
            HttpServletResponse httpResp,
            LoginCredential credential
    ) throws IOException, MissingServletRequestParameterException {
        AuthorizationRequest req = buildAuthorizationRequest(httpReq);
        String querystring = req.buildQuerystring();
        verifySignature(querystring, httpReq.getParameter("signature"));
        try {
            loginService.login(credential.getUsername(), credential.getPassword());
            httpResp.sendRedirect(hostUrlProvider.provide() + "/authorize?" + querystring);
        } catch (InvalidUsernamePasswordException ex) {
            httpResp.sendRedirect(hostUrlProvider.provide() + "/login?error=invalid_username_password&" + querystring + "&signature=" + httpReq.getParameter("signature"));
        } catch (LockUserException ex) {
            httpResp.sendRedirect(hostUrlProvider.provide() + "/login?error=lock_user&" + querystring + "&signature=" + httpReq.getParameter("signature"));
        }
    }
}
