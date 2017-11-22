/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.oauth2.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/09
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserCtrl {

    @GetMapping("/me")
    public User me() {
        return User.builder()
                .id(1L)
                .name("นาย สมชาย ใจดี")
                .build();
    }

}
