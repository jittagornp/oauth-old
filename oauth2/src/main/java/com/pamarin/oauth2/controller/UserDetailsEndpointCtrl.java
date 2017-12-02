/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.pamarin.oauth2.model.OAuth2UserDetails;
import java.util.Arrays;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/09
 */
@RestController
public class UserDetailsEndpointCtrl {

    @GetMapping("/userDetails")
    public OAuth2UserDetails getUserDetails() {
        return OAuth2UserDetails.builder()
                .id(UUID.randomUUID().toString())
                .name("นาย สมชาย ใจดี")
                .authorities(Arrays.asList("sso"))
                .client(
                        OAuth2UserDetails.Client.builder()
                                .id(UUID.randomUUID().toString())
                                .name("ระบบทดสอบ OAuth2")
                                .scopes(Arrays.asList("read"))
                                .build()
                )
                .build();
    }

}
