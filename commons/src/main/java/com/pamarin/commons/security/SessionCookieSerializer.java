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

/**
 *
 * @author jitta
 */
public class SessionCookieSerializer implements CookieSerializer {

    private final CookieSpecBuilder cookieSpecBuilder;

    public SessionCookieSerializer(CookieSpecBuilder cookieSpecBuilder) {
        if(!cookieSpecBuilder.isEncodeBase64Value()){
           cookieSpecBuilder.encodeBase64Value(); 
        }
        
        this.cookieSpecBuilder = cookieSpecBuilder;
    }

    @Override
    public void writeCookieValue(CookieValue cookieValue) {
        cookieValue.getResponse().addHeader("Set-Cookie", 
                cookieSpecBuilder.setValue(cookieValue.getCookieValue())
                        .build()
        );
    }

    @Override
    public List<String> readCookieValues(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        List<String> matchingCookieValues = new ArrayList<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (this.cookieSpecBuilder.getKey().equals(cookie.getName())) {
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

    private String base64Decode(String base64Value) {
        try {
            return new String(Base64.getDecoder().decode(base64Value));
        } catch (Exception e) {
            return null;
        }
    }

}
