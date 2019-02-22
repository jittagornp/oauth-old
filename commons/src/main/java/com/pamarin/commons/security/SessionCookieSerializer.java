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

    private final Base64AESEncryption aesEncryption = new DefaultBase64AESEncryption(DefaultAESEncryption.withKeyLength16());

    private final String secretKey;

    public SessionCookieSerializer(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void writeCookieValue(CookieValue cookieValue) {
        String value = cookieValue.getCookieValue();
        boolean hasValue = hasText(value);
        int maxAge = hasValue ? cookieMaxAge : -1;
        String token = hasValue ? aesEncryption.encrypt(value, secretKey) : null;
        cookieValue.getResponse().addHeader("Set-Cookie",
                new CookieSpecBuilder(cookieName)
                        .setHttpOnly(true)
                        .setSecure(secure)
                        .setPath("/")
                        .setValue(token)
                        .setMaxAge(maxAge)
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
                    if (!hasText(cookie.getValue())) {
                        continue;
                    }
                    matchingCookieValues.add(aesEncryption.decrypt(cookie.getValue(), secretKey));
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

}
