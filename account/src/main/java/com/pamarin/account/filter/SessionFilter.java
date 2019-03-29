/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.account.filter;

import com.pamarin.oauth2.client.sdk.OAuth2SessionRetriever;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author jitta
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SessionFilter extends OncePerRequestFilter {

    private final OAuth2SessionRetriever oauth2SessionRetriever;

    @Autowired
    public SessionFilter(OAuth2SessionRetriever oauth2SessionRetriever) {
        this.oauth2SessionRetriever = oauth2SessionRetriever;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpReq, HttpServletResponse httpResp, FilterChain chain) throws ServletException, IOException {
        oauth2SessionRetriever.retrieve(httpReq, httpResp);
        chain.doFilter(httpReq, httpResp);
    }
}
