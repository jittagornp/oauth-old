/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.util.QuerystringBuilder;
import java.io.IOException;
import static java.lang.String.format;
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

    @Autowired
    public LogoutController(HostUrlProvider hostUrlProvider, OAuth2ClientOperations clientOperations) {
        this.hostUrlProvider = hostUrlProvider;
        this.clientOperations = clientOperations;
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

    @GetMapping("/logout")
    public void logout(HttpServletResponse httpResp) throws IOException {
        httpResp.sendRedirect(getLogoutUrl());
    }

}
