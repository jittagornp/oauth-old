/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.util;

import java.util.Objects;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/19
 */
public class CookieSpecBuilder {

    private final String key;

    private final String value;

    private String path = "/";

    private Boolean httpOnly = Boolean.TRUE;

    private String sameSite;

    private Boolean secure;

    public CookieSpecBuilder(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public CookieSpecBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public CookieSpecBuilder setHttpOnly(Boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    /**
     * Because Cookie not support same site attribute
     *
     * @param s
     * @return
     */
    public CookieSpecBuilder setSameSite(String s) {
        this.sameSite = s;
        return this;
    }

    //https://www.owasp.org/index.php/SameSite
    public CookieSpecBuilder sameSiteStrict() {
        return this.setSameSite("Strict");
    }

    public CookieSpecBuilder setSecure(Boolean s) {
        this.secure = s;
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s=%s;", key, value));
        if (hasText(path)) {
            builder.append(String.format(" Path=%s;", path));
        }
        if (Objects.equals(Boolean.TRUE, httpOnly)) {
            builder.append(" HttpOnly;");
        }
        if (hasText(sameSite)) {
            builder.append(String.format(" SameSite=%s;", sameSite));
        }
        if (Objects.equals(Boolean.TRUE, secure)) {
            builder.append(" Secure;");
        }
        return builder.toString();
    }
}
