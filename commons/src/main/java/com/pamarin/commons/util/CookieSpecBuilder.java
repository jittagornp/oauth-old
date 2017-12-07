/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import static org.springframework.util.StringUtils.hasText;

/**
 * https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie
 *
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/19
 */
public class CookieSpecBuilder {

    private final String key;

    private final String value;

    private String path = "/";

    private Boolean httpOnly = Boolean.TRUE;

    private String sameSite;

    private Boolean secure;

    private LocalDateTime expires;

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

    public CookieSpecBuilder setExpires(LocalDateTime expires) {
        this.expires = expires;
        return this;
    }

    public String build() {
        List<String> builder = new ArrayList<>();
        builder.add(String.format("%s=%s", key, value));
        if (hasText(path)) {
            builder.add(String.format("Path=%s", path));
        }
        if (Objects.equals(Boolean.TRUE, httpOnly)) {
            builder.add("HttpOnly");
        }
        if (hasText(sameSite)) {
            builder.add(String.format("SameSite=%s", sameSite));
        }
        if (Objects.equals(Boolean.TRUE, secure)) {
            builder.add("Secure");
        }
        if (expires != null) {
            builder.add(String.format("Expires=%s", makeExpires()));
        }
        return builder.stream().collect(Collectors.joining("; "));
    }

    private String makeExpires() {
        //Wed, 21 Oct 2015 07:28:00 GMT 
        return expires.format(DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss 'GMT'"));
    }
}
