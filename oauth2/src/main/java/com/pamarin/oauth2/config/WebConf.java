/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.commons.security.AuthenticityToken;
import com.pamarin.commons.security.CsrfInterceptor;
import com.pamarin.oauth2.security.SessionCookieSerializer;
import com.pamarin.commons.security.DefaultAuthenticityToken;
import com.pamarin.commons.util.HttpAuthorizeBearerParser;
import com.pamarin.oauth2.RedisOAuth2AccessTokenRepo;
import com.pamarin.oauth2.RedisOAuth2RefreshTokenRepo;
import com.pamarin.oauth2.interceptor.UserSourceTokenInterceptor;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepo;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepo;
import com.pamarin.oauth2.resolver.DefaultUserSourceTokenIdResolver;
import com.pamarin.oauth2.resolver.UserSourceTokenIdResolver;
import com.pamarin.oauth2.service.AccessTokenVerification;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/26
 */
@Configuration
@EnableWebMvc
public class WebConf extends WebMvcConfigurerAdapter {

    @Value("${spring.session.timeout}")
    private Integer sessionTimeout;

    @Value("${spring.session.access-token.timeout}")
    private Integer accessTokenTimeout;

    @Value("${spring.session.refresh-token.timeout}")
    private Integer refreshTokenTimeout;

    @Value("${spring.session.secretKey}")
    private String secretKey;

    @Value("${server.hostUrl}")
    private String hostUrl;

    @Autowired
    private HttpAuthorizeBearerParser httpAuthorizeBearerParser;

    @Autowired
    private AccessTokenVerification accessTokenVerification;

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

    @Bean
    public CookieSerializer newCookieSerializer() {
        SessionCookieSerializer cookieSerializer = new SessionCookieSerializer(secretKey, httpAuthorizeBearerParser, accessTokenVerification);
        cookieSerializer.setCookieMaxAge(sessionTimeout);
        cookieSerializer.setCookieName("user-session");
        cookieSerializer.setSecure(hostUrl.startsWith("https://"));
        return cookieSerializer;
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
    public UserSourceTokenInterceptor newUserSourceInterceptor() {
        return new UserSourceTokenInterceptor(
                "user-source",
                newUserSourceTokenIdResolver()
        );
    }

    @Bean
    public UserSourceTokenIdResolver newUserSourceTokenIdResolver() {
        return new DefaultUserSourceTokenIdResolver("user-source");
    }

    @Bean
    public AuthenticityToken newAuthenticityToken() {
        return new DefaultAuthenticityToken(44);
    }

    @Bean
    public OAuth2AccessTokenRepo newOAuth2AccessTokenRepo() {
        return new RedisOAuth2AccessTokenRepo(accessTokenTimeout / 60);
    }

    @Bean
    public OAuth2RefreshTokenRepo newOAuth2RefreshTokenRepo() {
        return new RedisOAuth2RefreshTokenRepo(refreshTokenTimeout / 60);
    }
}
