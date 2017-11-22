/*
 * Copyright 2017 Pamarin.com
 */

package com.pamarin.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt;  
 * create : 2017/11/20
 */
@Configuration
@Profile("!test") //inactive for test profile
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PersistenceConf {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new ModifyUserAuditorAware();
    }
}

