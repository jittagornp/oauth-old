/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.commons.security.LoginSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author jitta
 */
@Controller
public class LogoutCtrl {

    @Autowired
    private LoginSession loginSession;

    @GetMapping("/logout")
    public void getLogout() {

        loginSession.logout();

    }

    @PostMapping("/logout")
    public void postLogout() {

        loginSession.logout();

    }

}
