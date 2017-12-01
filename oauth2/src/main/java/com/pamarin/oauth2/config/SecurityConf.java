/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.oauth2.service.LoginService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/19
 */
@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginService loginService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/authorize",
                        "/token",
                        "/login",
                        "/",
                        "/code/callback",
                        "/assets/**",
                        "/favicon.ico"
                )
                .permitAll()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .rememberMe()
                .key("1111111111111")
                .rememberMeServices(newRememberMeServices());
    } 

    @Bean
    public PersistentTokenBasedRememberMeServices newRememberMeServices() {
        PersistentTokenBasedRememberMeServices service =  new PersistentTokenBasedRememberMeServices(
                "1111111111111",
                loginService,
                new InMemoryTokenRepositoryImpl()
        );
        service.setParameter("rememberme");
        service.setCookieName("rmb");
        return service; 
    }
}
