/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.util.CookieSpecBuilder;
import com.pamarin.commons.util.DateConverterUtils;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

/**
 *
 * @author jitta
 */
public class CustomTokenBasedRememberMeServices extends TokenBasedRememberMeServices {

    public CustomTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        String cookieValue = encodeCookie(tokens);
        httpResp.addHeader("Set-Cookie", new CookieSpecBuilder(getCookieName(), cookieValue)
                .sameSiteStrict()
                //.setExpires(DateConverterUtils.convert2LocalDateTime(new Date(maxAge)))
                .setHttpOnly(true)
                .setPath("/")
                .setSecure(httpReq.isSecure())
                .build());
    }

}
