/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.interceptor;

import com.pamarin.commons.util.CookieSpecBuilder;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Stream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.apache.commons.lang.ArrayUtils.isEmpty;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/07
 */
public class SourceTokenInterceptor extends HandlerInterceptorAdapter {

    private static final String COOKIE_NAME = "source";

    @NotBlank
    @Value("${server.hostUrl}")
    private String hostUrl;

    @Override
    public void postHandle(HttpServletRequest httpReq, HttpServletResponse httpResp, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && !hasSourceCookie(httpReq)) {
            httpResp.setHeader("Set-Cookie", new CookieSpecBuilder(COOKIE_NAME, makeToken())
                    .setPath("/")
                    .setSecure(hostUrl.startsWith("https://"))
                    .sameSiteStrict()
                    .setExpires(LocalDateTime.now().plusYears(100))
                    .build());
        }
    }

    private String makeToken() {
        return Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes());
    }

    private boolean hasSourceCookie(HttpServletRequest httpReq) {
        Cookie[] cookies = httpReq.getCookies();
        if (isEmpty(cookies)) {
            return false;
        }

        return Stream.of(cookies)
                .anyMatch(cookie -> {
                    return cookie != null
                            && COOKIE_NAME.equals(cookie.getName());
                });
    }
}
