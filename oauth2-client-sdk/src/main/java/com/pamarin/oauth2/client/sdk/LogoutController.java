/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.provider.HostUrlProvider;
import java.io.IOException;
import static java.lang.String.format;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/logout")
    public void logout(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        clientOperations.post(
                format("%s/logout", clientOperations.getAuthorizationServerHostUrl()),
                null,
                String.class,
                accessTokenResolver.resolve(httpReq)
        );

        httpResp.sendRedirect(hostUrlProvider.provide());
    }

}
