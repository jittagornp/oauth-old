/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.account.filter;

import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.resolver.DefaultHttpCookieResolver;
import com.pamarin.commons.resolver.HttpCookieResolver;
import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.client.sdk.OAuth2AccessToken;
import com.pamarin.oauth2.client.sdk.OAuth2Client;
import com.pamarin.oauth2.client.sdk.OAuth2Session;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author jitta
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SessionFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(SessionFilter.class);

    @Autowired
    private OAuth2Client oauth2Client;

    @Autowired
    private LoginSession loginSession;

    private final HttpCookieResolver accessTokenCookieResovler = new DefaultHttpCookieResolver("access_token");

    private final HttpCookieResolver refreshTokenCookieResovler = new DefaultHttpCookieResolver("refresh_token");

    @Autowired
    private HostUrlProvider hostUrlProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest httpReq, HttpServletResponse httpResp, FilterChain chain) throws ServletException, IOException {
        LOG.debug("getSession...");
        getSession(httpReq, httpResp);
        chain.doFilter(httpReq, httpResp);
    }

    private void getSession(HttpServletRequest httpReq, HttpServletResponse httpResp) {
        String accessToken = accessTokenCookieResovler.resolve(httpReq);
        LOG.debug("accessToken cookie => {}", accessToken);
        if (hasText(accessToken)) {
            getSession(accessToken, httpReq);
            /*try {
                getSession(accessToken);
            } catch (HttpClientErrorException ex) {
                LOG.debug("statusCode => {}", ex.getStatusCode());
                LOG.debug("error => ", ex);
                if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    accessToken = getAccessToken(httpReq, httpResp);
                    if (hasText(accessToken)) {
                        getSession(accessToken);
                    }
                }
            }*/
        }
    }

    private void getSession(String accessToken, HttpServletRequest httpReq) {
        try {
            OAuth2Session session = oauth2Client.getSession(accessToken);
            LOG.debug("session => {}", session.getId());

            DefaultUserDetails userDetails = DefaultUserDetails.builder()
                    .username(session.getUser().getId())
                    .password(session.getUser().getId())
                    .build();

            SecurityContext context = new SecurityContextImpl();
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            context.setAuthentication(token);

            httpReq.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", context);
        } catch (HttpClientErrorException ex) {
            HttpSession session = httpReq.getSession(false);
            if(session != null){
                session.setAttribute("SPRING_SECURITY_CONTEXT", null);
            }
        }
    }

    private String getAccessToken(HttpServletRequest httpReq, HttpServletResponse httpResp) {
        String refreshToken = refreshTokenCookieResovler.resolve(httpReq);
        LOG.debug("refreshToken cookie => {}", refreshToken);
        OAuth2AccessToken accessToken = oauth2Client.getAccessTokenByRefreshToken(refreshToken);
        LOG.debug("new accessToken => {}", accessToken.getAccessToken());
        Cookie accessTokenCookie = buildCookie("access_token", accessToken.getAccessToken(), 60 * 60 * 24); //24 hours
        Cookie refreshTokenCookie = buildCookie("refresh_token", accessToken.getAccessToken(), 60 * 60 * 24 * 14); //14 days
        httpResp.addCookie(accessTokenCookie);
        httpResp.addCookie(refreshTokenCookie);
        return accessToken.getAccessToken();
    }

    private Cookie buildCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge); //24 hours
        cookie.setSecure(hostUrlProvider.provide().startsWith("https://"));
        return cookie;
    }
}
