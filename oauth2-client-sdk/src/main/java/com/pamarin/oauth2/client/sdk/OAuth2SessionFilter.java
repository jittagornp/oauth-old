/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.exception.AuthenticationException;
import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.util.Base64Utils;
import com.pamarin.commons.util.HttpAuthorizeBearerParser;
import com.pamarin.commons.util.QuerystringBuilder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author jitta
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OAuth2SessionFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(OAuth2SessionFilter.class);

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    private static final int ONE_DAY_SECONDS = 60 * 60 * 24;

    private static final int FOURTEEN_DAYS_SECONDS = ONE_DAY_SECONDS * 14;

    private static final int STATE_SIZE = 11;

    private final SecureRandom secureRandom = new SecureRandom();

    private final OAuth2ClientOperations oauth2ClientOperations;

    private final HostUrlProvider hostUrlProvider;

    private final OAuth2AccessTokenResolver oauth2AccessTokenResolver;

    private final OAuth2RefreshTokenResolver oauth2RefreshTokenResolver;

    private final HttpAuthorizeBearerParser httpAuthorizeBearerParser;

    @Autowired
    public OAuth2SessionFilter(
            OAuth2ClientOperations oauth2ClientOperations,
            HostUrlProvider hostUrlProvider,
            OAuth2AccessTokenResolver oauth2AccessTokenResolver,
            OAuth2RefreshTokenResolver oauth2RefreshTokenResolver,
            HttpAuthorizeBearerParser httpAuthorizeBearerParser
    ) {
        this.oauth2ClientOperations = oauth2ClientOperations;
        this.hostUrlProvider = hostUrlProvider;
        this.oauth2AccessTokenResolver = oauth2AccessTokenResolver;
        this.oauth2RefreshTokenResolver = oauth2RefreshTokenResolver;
        this.httpAuthorizeBearerParser = httpAuthorizeBearerParser;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpReq, HttpServletResponse httpResp, FilterChain chain) throws ServletException, IOException {
        try {
            doFilter(httpReq, httpResp);
            chain.doFilter(httpReq, httpResp);
        } catch (AuthenticationException ex) {
            httpResp.sendRedirect(getAuthorizationUrl(httpReq));
        } catch (RequireRedirectException ex) {
            httpResp.sendRedirect("/");
        }
    }

    private void doFilter(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String accessToken = getAccessTokenHeader(httpReq.getHeader("Authorization"));
        if (hasText(accessToken)) {
            if (!getSession(accessToken, httpReq, httpResp)) {
                error401Unauthorized(httpResp);
            }
        } else {
            String code = httpReq.getParameter("code");
            String state = httpReq.getParameter("state");
            if (hasText(code) && hasText(state)) {
                verifyAuthorizationState(state, httpReq);
                if (getAccessTokenByAuthorizationCode(code, httpReq, httpResp)) {
                    throw new RequireRedirectException("Get accessToken from authorizationCode success.");
                }
            } else {
                getSession(httpReq, httpResp);
            }
        }
    }

    private String randomState() {
        byte[] bytes = new byte[STATE_SIZE];
        secureRandom.nextBytes(bytes);
        return Base64Utils.encode(bytes);
    }

    private String getAuthorizationUrl(HttpServletRequest httpReq) throws UnsupportedEncodingException {
        String state = randomState();
        httpReq.getSession().setAttribute(OAuth2SdkConstant.OAUTH2_AUTHORIZATION_STATE, state);
        return "{server}/authorize?".replace("{server}", oauth2ClientOperations.getAuthorizationServerHostUrl())
                + new QuerystringBuilder()
                        .addParameter("response_type", "code")
                        .addParameter("client_id", oauth2ClientOperations.getClientId())
                        .addParameter("redirect_uri", hostUrlProvider.provide() + "/callback")
                        .addParameter("scope", oauth2ClientOperations.getScope())
                        .addParameter("state", state)
                        .build();
    }

    private String getAccessTokenHeader(String authorization) {
        if (!hasText(authorization)) {
            return null;
        }
        return httpAuthorizeBearerParser.parse(authorization);
    }

    private void verifyAuthorizationState(String state, HttpServletRequest httpReq) {
        HttpSession session = httpReq.getSession(false);
        if (session != null) {
            String sessionState = (String) session.getAttribute(OAuth2SdkConstant.OAUTH2_AUTHORIZATION_STATE);
            if (!Objects.equals(state, sessionState)) {
                clearSecurityContext(httpReq);
                throw new InvalidAuthorizationStateException("Invalid Authorization state " + state);
            }
        }
    }

    private void throwIfNotUnauthorized(HttpClientErrorException ex) {
        if (ex.getStatusCode() != HttpStatus.UNAUTHORIZED) {
            throw ex;
        }
    }

    private boolean getAccessTokenByAuthorizationCode(String authorizationCode, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        try {
            LOG.debug("authorizationCode => {}", authorizationCode);
            OAuth2AccessToken accessToken = oauth2ClientOperations.getAccessTokenByAuthorizationCode(authorizationCode);
            saveToken(accessToken, httpReq, httpResp);
            return true;
        } catch (HttpClientErrorException ex) {
            LOG.debug("getAccessToken error => {}", ex);
            clearSecurityContext(httpReq);
            throwIfNotUnauthorized(ex);
            return false;
        }
    }

    private void getSession(HttpServletRequest httpReq, HttpServletResponse httpResp) {
        String accessToken = oauth2AccessTokenResolver.resolve(httpReq);
        if (!getSession(accessToken, httpReq, httpResp)) {
            accessToken = refreshToken(httpReq, httpResp);
            if (!getSession(accessToken, httpReq, httpResp)) {
                throw new AuthenticationException("Please login");
            }
        }
    }

    private boolean getSession(String accessToken, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        if (!hasText(accessToken)) {
            clearSecurityContext(httpReq);
            return false;
        }

        try {
            LOG.debug("accessToken => {}", accessToken);
            OAuth2Session session = oauth2ClientOperations.getSession(accessToken);
            LOG.debug("loggedIn sessionId => {}", session.getId());
            saveSession(session, httpReq);
            return true;
        } catch (HttpClientErrorException ex) {
            LOG.debug("getSession error => {}", ex);
            clearSecurityContext(httpReq);
            throwIfNotUnauthorized(ex);
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
        String refreshToken = oauth2RefreshTokenResolver.resolve(httpReq);
        if (!hasText(refreshToken)) {
            return null;
        }

        try {
            LOG.debug("refreshToken => {}", refreshToken);
            OAuth2AccessToken accessToken = oauth2ClientOperations.getAccessTokenByRefreshToken(refreshToken);
            saveToken(accessToken, httpReq, httpResp);
            return accessToken.getAccessToken();
        } catch (HttpClientErrorException ex) {
            LOG.debug("refreshToken error => {}", ex);
            throwIfNotUnauthorized(ex);
            return null;
        }
    }

    private SecurityContext buildSecurityContext(OAuth2Session.User user) {
        DefaultUserDetails userDetails = DefaultUserDetails.builder()
                .username(user.getId())
                .password("")
                .authorities(user.getAuthorities())
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

    private void saveSession(OAuth2Session session, HttpServletRequest httpReq) {
        SecurityContext context = buildSecurityContext(session.getUser());
        HttpSession httpSession = httpReq.getSession(true);
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT, context);
        OAuth2SessionContext.setSession(session);
    }

    private void saveToken(OAuth2AccessToken accessToken, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        //cookie
        httpResp.addCookie(buildCookie(
                oauth2AccessTokenResolver.getTokenName(),
                accessToken.getAccessToken(),
                ONE_DAY_SECONDS
        ));

        httpResp.addCookie(buildCookie(
                oauth2RefreshTokenResolver.getTokenName(),
                accessToken.getRefreshToken(),
                FOURTEEN_DAYS_SECONDS
        ));

        //request attribute
        httpReq.setAttribute(
                oauth2RefreshTokenResolver.getTokenName(),
                accessToken.getAccessToken()
        );

        httpReq.setAttribute(
                oauth2RefreshTokenResolver.getTokenName(),
                accessToken.getRefreshToken()
        );
    }

    private Cookie buildCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(hostUrlProvider.provide().startsWith("https://"));
        return cookie;
    }

    private void error401Unauthorized(HttpServletResponse httpResp) throws IOException {
        httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
