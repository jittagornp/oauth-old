/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.account.config;

import com.pamarin.oauth2.client.sdk.AuthenticationEntryPointAdapter;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 *
 * @author jitta
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConf extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .authenticationEntryPoint(newAuthenticationEntryPoint())
                .and()
                .headers().frameOptions().disable()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/public/**",
                        "/static/**",
                        "/assets/**",
                        "/favicon.ico"
                )
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .logoutUrl("logout-" + UUID.randomUUID().toString()); //not publish logout url to outside
    }

    @Bean
    public AuthenticationEntryPoint newAuthenticationEntryPoint() {
        return new AuthenticationEntryPointAdapter() {

            @Value("${oauth2.authorization-server.hostUrl}")
            private String authorizationServerHostUrl;

            @Value("${server.hostUrl}")
            private String hostUrl;

            @Override
            protected String getAuthorizationServerHostUrl() {
                return authorizationServerHostUrl;
            }

            @Override
            protected String getHostUrl() {
                return hostUrl;
            }

            @Override
            protected String getClientId() {
                return "b98e21b4-ce2a-11e7-abc4-cec278b6b50a";
            }

            @Override
            protected String getScope() {
                return "user:public_profile";
            }

        };
    }
}
