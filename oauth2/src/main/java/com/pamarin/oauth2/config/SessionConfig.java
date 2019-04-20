/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.commons.util.HttpAuthorizeBearerParser;
import com.pamarin.oauth2.session.DatabaseSessionRepositoryImpl;
import com.pamarin.oauth2.security.SessionCookieSerializer;
import com.pamarin.oauth2.service.AccessTokenVerification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import javax.validation.constraints.NotNull;
import com.pamarin.oauth2.repository.DatabaseSessionRepository;

/**
 *
 * @author jitta
 */
@Configuration
public class SessionConfig {

    @NotNull
    @Value("${spring.session.timeout}")
    private Integer sessionTimeout;

    @Value("${spring.session.secretKey}")
    private String secretKey;

    @Value("${server.hostUrl}")
    private String hostUrl;

    @Bean
    public CookieSerializer newCookieSerializer(HttpAuthorizeBearerParser httpAuthorizeBearerParser, AccessTokenVerification accessTokenVerification) {
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

    @Bean
    public DatabaseSessionRepository newDatabaseSessionRepository() {
        return new DatabaseSessionRepositoryImpl(sessionTimeout, 1000 * 30);
    }

}
