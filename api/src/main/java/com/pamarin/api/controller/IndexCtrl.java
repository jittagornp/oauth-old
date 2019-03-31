/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.api.controller;

import com.pamarin.commons.view.ModelAndViewBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author jitta
 */
@Controller
public class IndexCtrl {

    @GetMapping({"", "/"})
    public ModelAndView index() {
        return new ModelAndViewBuilder()
                .setName("index")
                .build();
    }

}
