/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.commons.exception.InvalidSignatureException;
import com.pamarin.oauth2.converter.HttpServletRequest2AuthorizationRequestConverter;
import com.pamarin.oauth2.exception.InvalidUsernamePasswordException;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.model.LoginCredential;
import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.security.GetCsrfToken;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.commons.security.hashing.Hashing;
import com.pamarin.oauth2.service.AuthorizationRequestVerification;
import com.pamarin.commons.view.ModelAndViewBuilder;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import com.pamarin.oauth2.service.LoginService;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/24
 */
@Controller
public class LoginCtrl {

    private static final Logger LOG = LoggerFactory.getLogger(LoginCtrl.class);

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
    private Hashing hashing;

    private AuthorizationRequest buildAuthorizationRequest(HttpServletRequest httpReq) throws MissingServletRequestParameterException {
        AuthorizationRequest req = requestConverter.convert(httpReq);
        req.requireParameters();
        requestVerification.verify(req);
        return req;
    }

    private void verifySignature(String querystring, String signature) throws MissingServletRequestParameterException {
        if (!hasText(signature)) {
            throw new MissingServletRequestParameterException("signature", "String");
        }
        if (!hashing.matches(querystring.getBytes(), signature)) {
            throw new InvalidSignatureException("Invalid signature \"" + signature + "\".");
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
                .addAttribute("processUrl", hostUrlProvider.provide() + "/login" + (req.haveSomeParameters() ? ("?" + querystring) : ""))
                .build();
    }

    @PostMapping("/login")
    public void login(
            HttpServletRequest httpReq,
            HttpServletResponse httpResp,
            LoginCredential credential
    ) throws IOException, MissingServletRequestParameterException {
        AuthorizationRequest req = buildAuthorizationRequest(httpReq);
        try {
            loginService.login(credential.getUsername(), credential.getPassword());
            httpResp.sendRedirect(hostUrlProvider.provide() + "/authorize?" + req.buildQuerystring());
        } catch (InvalidUsernamePasswordException ex) {
            LOG.warn("Invalid username password ", ex);
            httpResp.sendRedirect(hostUrlProvider.provide() + "/login?error=invalid_username_password" + (req.haveSomeParameters() ? ("&" + req.buildQuerystring()) : ""));
        }
    }
}
