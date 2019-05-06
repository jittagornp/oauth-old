/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.util.QuerystringBuilder;
import java.io.IOException;
import static java.lang.String.format;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpClientErrorException;

/**
 *
 * @author jitta
 */
@Controller
public class LogoutController {

    private final HostUrlProvider hostUrlProvider;

    private final OAuth2ClientOperations clientOperations;

    private final OAuth2AccessTokenResolver accessTokenResolver;

    @Autowired
    public LogoutController(
            HostUrlProvider hostUrlProvider,
            OAuth2ClientOperations clientOperations,
            OAuth2AccessTokenResolver accessTokenResolver
    ) {
        this.hostUrlProvider = hostUrlProvider;
        this.clientOperations = clientOperations;
        this.accessTokenResolver = accessTokenResolver;
    }

    private String getLogoutUrl() {
        return format("%s/logout?%s",
                clientOperations.getAuthorizationServerHostUrl(),
                new QuerystringBuilder()
                        .addParameter("client_id", clientOperations.getClientId())
                        .addParameter("redirect_uri", hostUrlProvider.provide())
                        .build()
        );
    }

    private void logoutFromBackendService(HttpServletRequest httpReq) {
        clientOperations.post(
                format("%s/logout", clientOperations.getAuthorizationServerHostUrl()),
                null,
                String.class,
                accessTokenResolver.resolve(httpReq)
        );
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        try {
            logoutFromBackendService(httpReq);
            httpResp.sendRedirect(hostUrlProvider.provide());
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() != HttpStatus.UNAUTHORIZED) {
                throw ex;
            }

            httpResp.sendRedirect(getLogoutUrl());
        }
    }

}
