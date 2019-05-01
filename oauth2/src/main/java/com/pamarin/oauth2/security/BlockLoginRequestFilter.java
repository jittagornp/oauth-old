/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.security;

import com.pamarin.commons.exception.AuthorizationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.apache.commons.lang.ArrayUtils.isEmpty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author jitta
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BlockLoginRequestFilter extends OncePerRequestFilter {

    private static final List<String> HEADERS = Arrays.asList("User-Agent");

    private static final List<String> COOKIES = Arrays.asList("user-agent", "user-session");

    @Override
    protected void doFilterInternal(HttpServletRequest httpReq, HttpServletResponse httpResp, FilterChain chain) throws ServletException, IOException {
        if ("/login".equals(httpReq.getServletPath())) {
            if (!matchAllHeaders(httpReq)) {
                throw new AuthorizationException("Block request");
            }

            if (!matchAllCookies(httpReq.getCookies())) {
                throw new AuthorizationException("Block request");
            }
        }

        chain.doFilter(httpReq, httpResp);
    }

    private boolean matchAllHeaders(HttpServletRequest httpReq) {
        return HEADERS.stream()
                .noneMatch(headerName -> !hasText(httpReq.getHeader(headerName)));
    }

    private boolean matchAllCookies(Cookie[] cookies) {
        return COOKIES.stream()
                .noneMatch(cookieName -> !hasText(getCookieValue(cookies, cookieName)));
    }

    private String getCookieValue(Cookie[] cookies, String cookieName) {
        if (isEmpty(cookies)) {
            return null;
        }
        return Stream.of(cookies)
                .filter(cookie -> cookie != null && cookieName.equalsIgnoreCase(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
