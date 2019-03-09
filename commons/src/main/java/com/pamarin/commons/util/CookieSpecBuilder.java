/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
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

    private String value;

    private String path = "/";

    private Boolean httpOnly = Boolean.TRUE;

    private String sameSite;

    private Boolean secure;

    private LocalDateTime expires;

    private boolean encodeBase64Value = false;

    private int maxAge;

    public CookieSpecBuilder(String key) {
        this(key, null);
    }

    public CookieSpecBuilder(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public CookieSpecBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public CookieSpecBuilder encodeBase64Value() {
        this.encodeBase64Value = true;
        return this;
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

    public CookieSpecBuilder setMaxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public String build() {
        List<String> builder = new ArrayList<>();
        builder.add(String.format("%s=%s", getKey(), getValue()));
        if (hasText(getPath())) {
            builder.add(String.format("Path=%s", getPath()));
        }
        if (Objects.equals(Boolean.TRUE, getHttpOnly())) {
            builder.add("HttpOnly");
        }
        if (hasText(getSameSite())) {
            builder.add(String.format("SameSite=%s", getSameSite()));
        }
        if (Objects.equals(Boolean.TRUE, getSecure())) {
            builder.add("Secure");
        }
        if (getMaxAge() > 0) {
            builder.add(String.format("Max-Age=%s", getMaxAge()));
        }
        if (getExpires() != null) {
            builder.add(String.format("Expires=%s", makeExpires()));
        }
        return builder.stream().collect(Collectors.joining("; "));
    }

    private String makeExpires() {
        //Wed, 21 Oct 2015 07:28:00 GMT 
        return getExpires().format(DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss 'GMT'"));
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return isEncodeBase64Value() ? Base64.getEncoder().encodeToString(value.getBytes()) : value;
    }

    public String getPath() {
        return path;
    }

    public Boolean getHttpOnly() {
        return httpOnly;
    }

    public String getSameSite() {
        return sameSite;
    }

    public Boolean getSecure() {
        return secure;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public boolean isEncodeBase64Value() {
        return encodeBase64Value;
    }

    public int getMaxAge() {
        return maxAge;
    }

}
