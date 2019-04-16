/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.commons.exception.AuthenticationException;
import com.pamarin.commons.security.LoginSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/20
 */
@Configuration
@Profile("!test") //inactive for test profile
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PersistenceConfig {

    @Bean
    public AuditorAware<String> auditorProvider(LoginSession loginSession) {
        return new ModifyUserAuditorAware(loginSession);
    }

    public static class ModifyUserAuditorAware implements AuditorAware<String> {

        private static final String DEFAULT_USERNAME = "system";

        private final LoginSession loginSession;

        @Autowired
        public ModifyUserAuditorAware(LoginSession loginSession) {
            this.loginSession = loginSession;
        }

        @Override
        public String getCurrentAuditor() {
            try {
                return loginSession.getUserDetails().getUsername();
            } catch (AuthenticationException ex) {
                return DEFAULT_USERNAME;
            }
        }

    }
}
