/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.usermanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author jitta
 */
@Configuration
@EnableWebMvc
public class WebConf extends WebMvcConfigurerAdapter {

    @Value("${server.hostUrl}")
    private String hostUrl;

    @Bean
    public RestTemplate newRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CookieSerializer newCookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setUseBase64Encoding(true);
        cookieSerializer.setUseHttpOnlyCookie(true);
        cookieSerializer.setUseSecureCookie(hostUrl.startsWith("https://"));
        cookieSerializer.setCookieName("session");
        cookieSerializer.setDomainName(hostUrl);
        cookieSerializer.setCookiePath("/");
        return cookieSerializer;
    }

    @Bean
    public RequestContextListener newRequestContextListener() {
        return new RequestContextListener();
    }

}
