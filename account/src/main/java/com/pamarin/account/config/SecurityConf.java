/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.account.config;

import com.pamarin.oauth2.client.sdk.AuthenticationEntryPointAdapter;
import com.pamarin.oauth2.client.sdk.OAuth2SessionRetriever;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
                        "/favicon.ico",
                        "/callback"
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

    @Component
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public static class SessionFilter extends OncePerRequestFilter {

        @Autowired
        private OAuth2SessionRetriever oauth2SessionRetriever;

        @Override
        protected void doFilterInternal(HttpServletRequest httpReq, HttpServletResponse httpResp, FilterChain chain) throws ServletException, IOException {
            oauth2SessionRetriever.retrieve(httpReq, httpResp);
            chain.doFilter(httpReq, httpResp);
        }
    }
}
