/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.util.CookieSpecBuilder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.session.web.http.CookieSerializer;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
public class SessionCookieSerializer implements CookieSerializer {

    private String cookieName = "user-session";

    private int cookieMaxAge = -1;

    private boolean secure;

    @Override
    public void writeCookieValue(CookieValue cookieValue) {
        cookieValue.getResponse().addHeader("Set-Cookie",
                new CookieSpecBuilder(cookieName)
                        .encodeBase64Value()
                        .setHttpOnly(true)
                        .setSecure(secure)
                        .setPath("/")
                        .setValue(cookieValue.getCookieValue())
                        .setMaxAge(hasText(cookieValue.getCookieValue()) ? cookieMaxAge : -1)
                        .build()
        );
    }

    @Override
    public List<String> readCookieValues(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        List<String> matchingCookieValues = new ArrayList<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (this.cookieName.equals(cookie.getName())) {
                    String sessionId = base64Decode(cookie.getValue());
                    if (sessionId == null) {
                        continue;
                    }
                    matchingCookieValues.add(sessionId);
                }
            }
        }
        return matchingCookieValues;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public void setCookieMaxAge(int cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    private String base64Decode(String base64Value) {
        try {
            return new String(Base64.getDecoder().decode(base64Value));
        } catch (Exception e) {
            return null;
        }
    }

}
