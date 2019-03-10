/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.commons.util.HttpAuthorizeBearerParser;
import com.pamarin.oauth2.security.SessionCookieSerializer;
import com.pamarin.oauth2.service.AccessTokenVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;

/**
 *
 * @author jitta
 */
@Configuration
public class SessionConf {

    @Value("${spring.session.timeout}")
    private Integer sessionTimeout;

    @Value("${spring.session.secretKey}")
    private String secretKey;

    @Value("${server.hostUrl}")
    private String hostUrl;

    @Autowired
    private HttpAuthorizeBearerParser httpAuthorizeBearerParser;

    @Autowired
    private AccessTokenVerification accessTokenVerification;

    @Bean
    public CookieSerializer newCookieSerializer() {
        SessionCookieSerializer cookieSerializer = new SessionCookieSerializer(
                "user-session",
                secretKey,
                httpAuthorizeBearerParser,
                accessTokenVerification
        );

        cookieSerializer.setCookieMaxAge(sessionTimeout);
        cookieSerializer.setSecure(hostUrl.startsWith("https://"));
        return cookieSerializer;
    }

}
