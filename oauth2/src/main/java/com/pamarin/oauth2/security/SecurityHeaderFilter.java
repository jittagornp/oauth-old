/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/13
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class SecurityHeaderFilter implements Filter {

    private static final int STS_MAX_AGE_A_YEAR = 60 * 60 * 24 * 365; //1 year

    @NotBlank
    @Value("${server.hostUrl}")
    private String serverHostUrl;

    @NotBlank
    @Value("${secure.strict-transport-security.allow-source}")
    private String allowSource;

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;

        httpResp.addHeader("X-Download-Options", "noopen");
        httpResp.setHeader("Content-Security-Policy", "default-src " + allowSource + " 'unsafe-eval' 'unsafe-inline'; object-src 'none'");
        if (serverHostUrl.startsWith("https://")) {
            httpResp.setHeader("Strict-Transport-Security", "max-age=" + STS_MAX_AGE_A_YEAR + "; includeSubDomains; preload;");
            httpResp.setHeader("Referrer-Policy", "no-referrer-when-downgrade");
        }
        httpResp.setHeader("X-Frame-Options", "SAMEORIGIN");
        chain.doFilter(httpReq, httpResp);
    }

    @Override
    public void destroy() {

    }

}
