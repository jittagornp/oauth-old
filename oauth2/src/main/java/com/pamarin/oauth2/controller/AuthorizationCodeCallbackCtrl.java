/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.commons.view.ModelAndViewBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/08
 */
@Controller
public class AuthorizationCodeCallbackCtrl {

    @GetMapping("/code/callback")
    public ModelAndView callback(
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "state", required = false) String state,
            @RequestParam(name = "error", required = false) String error,
            @RequestParam(name = "error_description", required = false) String errorDescription) {
        return new ModelAndViewBuilder()
                .setName("code-callback")
                .addAttribute("code", code)
                .addAttribute("state", state)
                .addAttribute("error", error)
                .addAttribute("errorDescription", errorDescription)
                .build();
    }

}
