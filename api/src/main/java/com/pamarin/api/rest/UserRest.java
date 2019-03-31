/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.api.rest;

import com.pamarin.oauth2.client.sdk.OAuth2Session.User;
import com.pamarin.oauth2.client.sdk.OAuth2SessionContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jitta
 */
@RestController
public class UserRest {

    @GetMapping("/me")
    public User getUser() {
        return OAuth2SessionContext.getSession().getUser();
    }

}
