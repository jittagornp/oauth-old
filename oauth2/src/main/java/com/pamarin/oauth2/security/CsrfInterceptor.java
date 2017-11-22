/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.security;

import com.pamarin.commons.security.AuthenticityToken;
import com.pamarin.commons.util.CookieSpecBuilder;
import com.pamarin.oauth2.exception.InvalidCsrfTokenException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/17
 */
public class CsrfInterceptor extends HandlerInterceptorAdapter {

    private static final String CSRF_COOKIE_KEY = "csrf-token";
    private static final String CSRF_PARAM_VALUE = "csrf-token";
    private static final String CSRF_ATTRIBUTE_KEY = "csrfParam";
    private static final String CSRF_ATTRIBUTE_VALUE = "csrfToken";
    private static final String CSRF_HEADER_KEY = "X-CSRF-Token";

    @NotBlank
    @Value("${server.hostUrl}")
    private String hostUrl;

    @Autowired
    private AuthenticityToken authenticityToken;

    private boolean checkCsrf(String httpMethod) {
        Pattern pattern = Pattern.compile("[POST|PUT|DELETE|PATCH]+", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(httpMethod).matches();
    }

    @Override
    public boolean preHandle(HttpServletRequest httpReq, HttpServletResponse httpResp, Object handler) throws Exception {
        if (!checkCsrf(httpReq.getMethod())) {
            return true;
        }

        String csrfToken = getCsrfToken(httpReq);
        String csrfCookie = getCsrfCookie(httpReq);

        doubleSubmitCookie(csrfToken, csrfCookie);
        synchronizerCSRFTokens(csrfToken, httpReq);

        return true;
    }

    private void synchronizerCSRFTokens(String csrfToken, HttpServletRequest httpReq) {
        String token = authenticityToken.decode(csrfToken);
        if (!hasText(token)) {
            throw new InvalidCsrfTokenException("Can't decode csrf token");
        }

        if (getCsrfSession(httpReq, token) == null) {
            throw new InvalidCsrfTokenException("Not found csrf token in user session");
        }
    }

    private void doubleSubmitCookie(String csrfToken, String csrfCookie) {
        if (csrfToken == null || csrfCookie == null) {
            throw new InvalidCsrfTokenException("Invalid Double Submit Cookie, don't have token");
        }

        //protect Timing Attacks against String Comparison
        if (!MessageDigest.isEqual(csrfToken.getBytes(), csrfCookie.getBytes())) {
            throw new InvalidCsrfTokenException("Invalid Double Submit Cookie");
        }
    }

    private Object getCsrfSession(HttpServletRequest httpReq, String token) {
        return httpReq.getSession().getAttribute(getCsrfSessionKey(token));
    }

    private String getCsrfToken(HttpServletRequest httpReq) {
        String csrfToken = httpReq.getParameter(CSRF_HEADER_KEY);
        if (!hasText(csrfToken)) {
            csrfToken = httpReq.getHeader(CSRF_HEADER_KEY);
        }
        return csrfToken;
    }

    private String getCsrfCookie(HttpServletRequest httpReq) {
        if (httpReq.getCookies() == null) {
            return null;
        }
        return Arrays.stream(httpReq.getCookies())
                .filter(c -> CSRF_PARAM_VALUE.equals(c.getName()))
                .findFirst()
                .map(c -> c.getValue())
                .orElse(null);
    }

    private boolean isHandlerMethod(Object handler) {
        return handler instanceof HandlerMethod;
    }

    private boolean hasAnnotation(HandlerMethod handlerMethod, Class annotaion) {
        Method method = handlerMethod.getMethod();
        return method.getAnnotation(annotaion) != null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && isHandlerMethod(handler)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (hasAnnotation(handlerMethod, GetCsrfToken.class)) {
                AuthenticityToken.RandomOutput random = authenticityToken.random();
                modelAndView.addObject(CSRF_ATTRIBUTE_KEY, CSRF_HEADER_KEY);
                modelAndView.addObject(CSRF_ATTRIBUTE_VALUE, random.getAuthenticityToken());
                saveToken(random, request.getSession());
                addTokenCookieAndHeader(random.getAuthenticityToken(), request, response);
            }
        }
    }

    private void saveToken(AuthenticityToken.RandomOutput random, HttpSession session) {
        session.setAttribute(getCsrfSessionKey(random.getToken()), true);
    }

    /**
     * https://www.owasp.org/index.php/Cross-Site_Request_Forgery_(CSRF)_Prevention_Cheat_Sheet
     *
     * Add the CSRF token cookie and header to the provided HTTP response object
     *
     * @param httpReq Source HTTP request
     * @param httpResponse HTTP response object to update
     */
    private void addTokenCookieAndHeader(String token, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        httpResp.setHeader(CSRF_HEADER_KEY, token);
        httpResp.addHeader("Set-Cookie", new CookieSpecBuilder(CSRF_COOKIE_KEY, token)
                .setPath(httpReq.getRequestURI())
                .setSecure(hostUrl.startsWith("https://"))
                .sameSiteStrict()
                .build());
    }

    private String getCsrfSessionKey(String token) {
        return CSRF_HEADER_KEY + ":" + token;
    }

}
