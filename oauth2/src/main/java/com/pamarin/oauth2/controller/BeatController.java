/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.security.DefaultHttpRequestSameOriginVerification;
import com.pamarin.commons.security.HttpRequestSameOriginVerification;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author jitta
 */
@Slf4j
@Controller
public class BeatController {

    private final HttpRequestSameOriginVerification sameOriginVerification;

    @Autowired
    public BeatController(HostUrlProvider hostUrlProvider) {
        this.sameOriginVerification = new DefaultHttpRequestSameOriginVerification(hostUrlProvider.provide());
    }

    @PostMapping("/beat")
    public void beat(HttpServletRequest httpReq) {
        sameOriginVerification.verify(httpReq);
        HttpSession session = httpReq.getSession(false);
        if (session != null) {
            log.debug("beat session.id => \"{}}\"", session.getId());
        }
    }

}
