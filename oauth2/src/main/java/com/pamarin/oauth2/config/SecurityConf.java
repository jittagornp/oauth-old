/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.commons.security.DefaultHashBasedToken;
import com.pamarin.commons.security.HashBasedToken;
import com.pamarin.commons.security.SHA384CheckSum;
import com.pamarin.oauth2.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/19
 */
@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {

    private static final String REMEMBER_ME_KEY = "u-)'y<+35xmDbpP.";
    
    private static final String HASHBASED_KEY = "u-)'y<+35xmDbpP.";

    @Autowired
    private LoginService loginService;

    @Value("${server.hostUrl}")
    private String hostUrl;

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
                        "/session",
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
                .key(REMEMBER_ME_KEY)
                .rememberMeServices(newRememberMeServices());
    }

    @Bean
    public RememberMeServices newRememberMeServices() {
        TokenBasedRememberMeServices service = new TokenBasedRememberMeServices(
                REMEMBER_ME_KEY, 
                loginService
        );
        service.setParameter("remember-me");
        service.setCookieName("rmbm");
        service.setUseSecureCookie(hostUrl.startsWith("https://"));
        return service;
    }

    @Bean
    public PersistentTokenRepository newPersistentTokenRepository() {
        return new InMemoryTokenRepositoryImpl();
    }
    
    @Bean
    public HashBasedToken newHashBasedToken(){
        return new DefaultHashBasedToken(
                HASHBASED_KEY, 
                new SHA384CheckSum()
        );
    }
}
