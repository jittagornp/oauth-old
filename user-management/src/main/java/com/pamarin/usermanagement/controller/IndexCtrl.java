/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.usermanagement.controller;

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
