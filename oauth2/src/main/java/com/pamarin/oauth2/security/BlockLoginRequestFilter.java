/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.security;

import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author jitta
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BlockLoginRequestFilter extends OncePerRequestFilter {

    private static final List<String> HEADERS = Arrays.asList("User-Agent");

    private final HttpClientIPAddressResolver httpClientIPAddressResolver;

    @Autowired
    public BlockLoginRequestFilter(HttpClientIPAddressResolver httpClientIPAddressResolver) {
        this.httpClientIPAddressResolver = httpClientIPAddressResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpReq, HttpServletResponse httpResp, FilterChain chain) throws ServletException, IOException {
        if ("/login".equals(httpReq.getServletPath())) {
            if (!matchAllHeaders(httpReq)) {
                log.debug("Not match some http headers.");
                httpResp.sendError(HttpServletResponse.SC_FORBIDDEN, getMessage(httpReq));
            }
        }

        chain.doFilter(httpReq, httpResp);
    }

    private String getMessage(HttpServletRequest httpReq) {
        return "Block request for IP address \"" + httpClientIPAddressResolver.resolve(httpReq) + "\".";
    }

    private boolean matchAllHeaders(HttpServletRequest httpReq) {
        return HEADERS.stream()
                .noneMatch(headerName -> !hasText(httpReq.getHeader(headerName)));
    }
}
