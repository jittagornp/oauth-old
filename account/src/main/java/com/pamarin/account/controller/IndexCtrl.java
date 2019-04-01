/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.account.controller;

import com.pamarin.commons.view.ModelAndViewBuilder;
import com.pamarin.oauth2.client.sdk.OAuth2AccessTokenResolver;
import com.pamarin.oauth2.client.sdk.OAuth2ClientOperations;
import com.pamarin.oauth2.client.sdk.OAuth2Session.User;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author jitta
 */
@Controller
public class IndexCtrl {

    @Autowired
    private OAuth2ClientOperations oauth2ClientOperations;

    @Autowired
    private OAuth2AccessTokenResolver oauth2AccessTokenResolver;

    @GetMapping({"", "/"})
    public ModelAndView index(HttpServletRequest httpReq) {

        String acccessToken = oauth2AccessTokenResolver.resolve(httpReq);
        User user = oauth2ClientOperations.get(
                "https://api-pamarin.herokuapp.com/me",
                User.class,
                acccessToken
        );

        return new ModelAndViewBuilder()
                .setName("index")
                .addAttribute("me", user)
                .build();
    }

}
