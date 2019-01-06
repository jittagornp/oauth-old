/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.controller.test;

import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.view.ModelAndViewBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/08
 */
@Controller
public class HomeCtrl {
    
    private static final String CLIENT_ID = "b98e21b4-ce2a-11e7-abc4-cec278b6b50a";

    @Autowired
    private HostUrlProvider hostUrlProvider;

    private String getExampleAuthorizeUrl() throws UnsupportedEncodingException {
        return "{host}/authorize?response_type=code&client_id={client_id}&redirect_uri={redirect_uri}&scope={scope}&state={state}"
                .replace("{host}", hostUrlProvider.provide())
                .replace("{client_id}", CLIENT_ID)
                .replace("{redirect_uri}", URLEncoder.encode(hostUrlProvider.provide() + "/code/callback", "utf-8"))
                .replace("{scope}", "user:public_profile")
                .replace("{state}", RandomStringUtils.randomAlphanumeric(8));
    }

    @GetMapping({"", "/"})
    public ModelAndView home() throws UnsupportedEncodingException {
        return new ModelAndViewBuilder()
                .setName("index")
                .addAttribute("exampleAuthorizeUrl", getExampleAuthorizeUrl())
                .build();
    }

}
