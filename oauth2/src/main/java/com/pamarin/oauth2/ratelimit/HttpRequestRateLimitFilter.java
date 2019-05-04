/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.ratelimit;

import com.pamarin.oauth2.exception.RateLimitException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author jitta
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpRequestRateLimitFilter extends OncePerRequestFilter {

    private final HttpRequestRateLimitService httpRequestRateLimitService;

    @Autowired
    public HttpRequestRateLimitFilter(@Qualifier("defaultTokenBucketRepository") TokenBucketRepository tokenBucketRepository) {
        this.httpRequestRateLimitService = new DefaultHttpRequestRateLimitService(tokenBucketRepository);
    }

    private boolean ignoreFor(HttpServletRequest httpReq) {
        String servletPath = httpReq.getServletPath();
        return "".equals(servletPath)
                || "/".equals(servletPath)
                || servletPath.startsWith("/static/")
                || servletPath.startsWith("/assets/")
                || "/favicon.ico".equals(servletPath);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpReq, HttpServletResponse httpResp, FilterChain chain) throws ServletException, IOException {
        if (ignoreFor(httpReq)) {
            chain.doFilter(httpReq, httpResp);
        } else {
            try {
                httpRequestRateLimitService.limit(httpReq);
                chain.doFilter(httpReq, httpResp);
            } catch (RateLimitException ex) {
                log.warn(null, ex);
                httpResp.sendError(429, "Too many requests, " + ex.getMessage());
            }
        }
    }

}
