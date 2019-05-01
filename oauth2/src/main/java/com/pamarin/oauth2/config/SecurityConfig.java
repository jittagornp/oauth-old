/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.commons.security.DefaultHashBasedToken;
import com.pamarin.commons.security.HashBasedToken;
import com.pamarin.commons.security.hashing.Hashing;
import com.pamarin.commons.security.hashing.HmacSHA256Hashing;
import com.pamarin.commons.security.hashing.ShortHashing;
import com.pamarin.commons.security.hashing.StringSignature;
import com.pamarin.commons.security.hashing.StringSignatureAdapter;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/19
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @NotNull
    @Value("${spring.token.secretKey}")
    private String tokenSecretKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/assets/**", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http/*.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()*/
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/authorize",
                        "/token",
                        "/login",
                        "/logout",
                        "/session",
                        "/",
                        "/code/callback",
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
    public HashBasedToken newHashBasedToken() {
        return new DefaultHashBasedToken(newHashing());
    }

    @Bean
    public Hashing newHashing() {
        return new HmacSHA256Hashing(tokenSecretKey);
    }

    @Bean
    public StringSignature newStringSignature() {
        final Hashing hashing = new ShortHashing(newHashing(), 19);
        return new StringSignatureAdapter() {
            @Override
            protected Hashing getHashing() {
                return hashing;
            }
        };
    }
}
