/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/21
 */
@Getter
@Setter
public class LoginCredential {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
