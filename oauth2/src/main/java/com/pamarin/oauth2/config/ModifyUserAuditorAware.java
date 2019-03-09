/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.commons.exception.AuthenticationException;
import com.pamarin.commons.security.LoginSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/20
 */
public class ModifyUserAuditorAware implements AuditorAware<String> {

    private static final String DEFAULT_USERNAME = "system";

    @Autowired
    private LoginSession loginSession;

    @Override
    public String getCurrentAuditor() {
        try {
            return loginSession.getUserDetails().getUsername();
        } catch (AuthenticationException ex) {
            return DEFAULT_USERNAME;
        }
    }

}
