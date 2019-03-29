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
import com.pamarin.oauth2.client.sdk.OAuth2Session.User;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
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

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
    private static final String ACCESS_TOKEN_NAME = "access_token";
    private static final String REFRESH_TOKEN_NAME = "refresh_token";

    private static final int ONE_DAY = 60 * 60 * 24;

    private final HttpCookieResolver accessTokenCookieResovler = new DefaultHttpCookieResolver(ACCESS_TOKEN_NAME);

    private final HttpCookieResolver refreshTokenCookieResovler = new DefaultHttpCookieResolver(REFRESH_TOKEN_NAME);

    private final OAuth2Client oauth2Client;

    private final HostUrlProvider hostUrlProvider;

    @Autowired
    public SessionFilter(OAuth2Client oauth2Client, HostUrlProvider hostUrlProvider) {
        this.oauth2Client = oauth2Client;
        this.hostUrlProvider = hostUrlProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpReq, HttpServletResponse httpResp, FilterChain chain) throws ServletException, IOException {
        LOG.debug("getSession...");
        getSession(httpReq, httpResp);
        chain.doFilter(httpReq, httpResp);
    }

    private void getSession(HttpServletRequest httpReq, HttpServletResponse httpResp) {
        String accessToken = accessTokenCookieResovler.resolve(httpReq);
        if (!getOAuth2Session(accessToken, httpReq, httpResp)) {
            accessToken = refreshToken(httpReq, httpResp);
            getOAuth2Session(accessToken, httpReq, httpResp);
        }
    }

    private SecurityContext buildSecurityContext(User user) {
        DefaultUserDetails userDetails = DefaultUserDetails.builder()
                .username(user.getId())
                .password(user.getId())
                .build();

        SecurityContext context = new SecurityContextImpl();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        context.setAuthentication(token);
        return context;
    }

    private boolean getOAuth2Session(String accessToken, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        if (!hasText(accessToken)) {
            clearSecurityContext(httpReq);
            return false;
        }

        try {
            LOG.debug("accessToken => {}", accessToken);
            OAuth2Session session = oauth2Client.getSession(accessToken);
            LOG.debug("loggedIn sessionId => {}", session.getId());
            SecurityContext context = buildSecurityContext(session.getUser());
            httpReq.getSession(true).setAttribute(SPRING_SECURITY_CONTEXT, context);
            return true;
        } catch (HttpClientErrorException ex) {
            LOG.debug("getOAuth2Session error => {}", ex);
            clearSecurityContext(httpReq);
            return false;
        }
    }

    private void clearSecurityContext(HttpServletRequest httpReq) {
        HttpSession session = httpReq.getSession(false);
        if (session != null) {
            session.setAttribute(SPRING_SECURITY_CONTEXT, null);
        }
    }

    private String refreshToken(HttpServletRequest httpReq, HttpServletResponse httpResp) {
        String refreshToken = refreshTokenCookieResovler.resolve(httpReq);
        if (!hasText(refreshToken)) {
            return null;
        }

        LOG.debug("refreshToken => {}", refreshToken);
        try {
            OAuth2AccessToken accessToken = oauth2Client.getAccessTokenByRefreshToken(refreshToken);
            addToCookie(accessToken, httpResp);
            return accessToken.getAccessToken();
        } catch (HttpClientErrorException ex) {
            LOG.debug("refreshToken error => {}", ex);
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return null;
            }
            throw ex;
        }
    }

    private void addToCookie(OAuth2AccessToken accessToken, HttpServletResponse httpResp) {
        httpResp.addCookie(buildCookie(ACCESS_TOKEN_NAME, accessToken.getAccessToken(), ONE_DAY));
        httpResp.addCookie(buildCookie(REFRESH_TOKEN_NAME, accessToken.getAccessToken(), ONE_DAY * 14));
    }

    private Cookie buildCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge); //24 hours
        cookie.setSecure(hostUrlProvider.provide().startsWith("https://"));
        return cookie;
    }
}
