/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.util.CookieSpecBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

/**
 *
 * @author jitta
 */
public class CustomTokenBasedRememberMeServices extends TokenBasedRememberMeServices {
    
    public boolean useSecureCookie = false;

    public CustomTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        String cookieValue = encodeCookie(tokens);
        httpResp.addHeader("Set-Cookie", new CookieSpecBuilder(getCookieName(), cookieValue)
                .sameSiteStrict()
                .setMaxAge(maxAge)
                .setHttpOnly(true)
                .setPath("/")
                .setSecure(useSecureCookie)
                .build());
    }

    @Override
    public void setUseSecureCookie(boolean useSecureCookie) {
        super.setUseSecureCookie(useSecureCookie);
        this.useSecureCookie = useSecureCookie;
    }

    
}
