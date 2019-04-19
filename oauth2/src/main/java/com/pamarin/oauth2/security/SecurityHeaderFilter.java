/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.security;

import com.pamarin.commons.provider.HostUrlProvider;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/13
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class SecurityHeaderFilter extends OncePerRequestFilter {

    private static final int STS_MAX_AGE_A_YEAR = 60 * 60 * 24 * 365; //1 year

    @NotBlank
    @Value("${secure.strict-transport-security.allow-source}")
    private String allowSource;

    private final boolean isSecure;

    @Autowired
    public SecurityHeaderFilter(HostUrlProvider hostUrlProvider) {
        isSecure = hostUrlProvider.provide().startsWith("https://");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpReq, HttpServletResponse httpResp, FilterChain chain) throws ServletException, IOException {
        httpResp.addHeader("X-Download-Options", "noopen");
        httpResp.setHeader("Content-Security-Policy", "default-src " + allowSource + " 'unsafe-eval' 'unsafe-inline'; object-src 'none'");
        if (isSecure) {
            httpResp.setHeader("Strict-Transport-Security", "max-age=" + STS_MAX_AGE_A_YEAR + "; includeSubDomains; preload;");
            httpResp.setHeader("Referrer-Policy", "no-referrer-when-downgrade");
        }
        httpResp.setHeader("X-Frame-Options", "SAMEORIGIN");
        chain.doFilter(httpReq, httpResp);
    }
}
