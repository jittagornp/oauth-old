/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.commons.view.ModelAndViewBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/08
 */
@Controller
public class HomeController {

    @GetMapping({"", "/"})
    public ModelAndView home() {
        return new ModelAndViewBuilder()
                .setName("index")
                .build();
    }

}
