/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.oauth2.converter.HttpServletRequest2AuthorizationRequestConverter;
import com.pamarin.oauth2.exception.RequireApprovalException;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.security.GetCsrfToken;
import com.pamarin.oauth2.service.AuthorizationService;
import com.pamarin.oauth2.service.AuthorizeViewModelService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/24
 */
@Controller
@RequestMapping("/authorize")
public class AuthorizeEndpointCtrl {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizeEndpointCtrl.class);

    @Autowired
    private HttpServletRequest2AuthorizationRequestConverter requestConverter;

    @Autowired
    private HostUrlProvider hostUrlProvider;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private AuthorizeViewModelService authorizeViewModelService;

    private AuthorizationRequest buildAuthorizationRequest(HttpServletRequest httpReq) throws MissingServletRequestParameterException {
        AuthorizationRequest req = requestConverter.convert(httpReq);
        req.requireParameters();
        return req;
    }

    @GetCsrfToken
    @GetMapping
    public ModelAndView authorize(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException, MissingServletRequestParameterException {
        AuthorizationRequest req = buildAuthorizationRequest(httpReq);
        try {
            httpResp.sendRedirect(authorizationService.authorize(req));
            return null;
        } catch (RequireApprovalException ex) {
            LOG.debug(null, ex);
            return new ModelAndViewBuilder()
                    .setName("authorize")
                    .addAttribute("processUrl", hostUrlProvider.provide() + "/authorize?" + req.buildQuerystring())
                    .addAttribute("model", authorizeViewModelService.findByClientIdAndScopes(req.getClientId(), req.getScopes()))
                    .build();
        }
    }

    @PostMapping(params = "answer=approved")
    public void approved(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException, MissingServletRequestParameterException {
        httpResp.sendRedirect(authorizationService.approved(buildAuthorizationRequest(httpReq)));
    }

    @PostMapping(params = "answer=not_approve")
    public void notApprove(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException, MissingServletRequestParameterException {
        httpResp.sendRedirect(authorizationService.notApprove(buildAuthorizationRequest(httpReq)));
    }
}
