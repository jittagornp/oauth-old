/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.commons.security.AuthenticityToken;
import com.pamarin.commons.security.CsrfInterceptor;
import com.pamarin.commons.security.DefaultAuthenticityToken;
import com.pamarin.oauth2.interceptor.UserAgentTokenInterceptor;
import com.pamarin.oauth2.resolver.DefaultUserAgentTokenIdResolver;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import com.pamarin.oauth2.resolver.UserAgentTokenIdResolver;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/26
 */
@Configuration
@EnableWebMvc
public class WebConf extends WebMvcConfigurerAdapter {

    @Value("${server.hostUrl}")
    private String hostUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/authorize").allowedOrigins("*");
        registry.addMapping("/session").allowedOrigins("*");
        registry.addMapping("/token").allowedOrigins("*");
        registry.addMapping("/logout").allowedOrigins("*");
        registry.addMapping("/login").allowedOrigins(hostUrl);
    }

    @Bean
    public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
        return new ResourceUrlEncodingFilter();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/assets/favicon.ico")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));

        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .resourceChain(true)
                .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"))
                .addTransformer(new AppCacheManifestTransformer());
    }

    @Bean
    public RestTemplate newRestTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(newCsrfInterceptor());
        registry.addInterceptor(newUserSourceInterceptor());
    }

    @Bean
    public CsrfInterceptor newCsrfInterceptor() {
        CsrfInterceptor interceptor = new CsrfInterceptor();
        interceptor.setIgnorePaths(
                "/",
                "/token",
                "/session",
                "/logout"
        );
        return interceptor;
    }

    @Bean
    public UserAgentTokenInterceptor newUserSourceInterceptor() {
        return new UserAgentTokenInterceptor(
                "user-agent",
                newUserAgentTokenIdResolver()
        );
    }

    @Bean
    public UserAgentTokenIdResolver newUserAgentTokenIdResolver() {
        return new DefaultUserAgentTokenIdResolver("user-agent");
    }

    @Bean
    public AuthenticityToken newAuthenticityToken() {
        return new DefaultAuthenticityToken(44);
    }

    @Bean
    public RequestContextListener newRequestContextListener() {
        return new RequestContextListener();
    }
}
