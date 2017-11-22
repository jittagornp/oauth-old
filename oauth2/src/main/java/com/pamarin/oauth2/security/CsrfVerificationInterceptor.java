/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.security;

import com.pamarin.oauth2.exception.InvalidCsrfTokenException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/17
 */
public class CsrfVerificationInterceptor extends HandlerInterceptorAdapter {

    private static final String CSRF_PARAM = "X-CSRF-Token";
    private static final String CSRF_COOKIE = "csrf-token";

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
        return httpReq.getSession().getAttribute(CSRF_PARAM + ":" + token);
    }

    private String getCsrfToken(HttpServletRequest httpReq) {
        String csrfToken = httpReq.getParameter(CSRF_PARAM);
        if (!hasText(csrfToken)) {
            csrfToken = httpReq.getHeader(CSRF_PARAM);
        }
        return csrfToken;
    }

    private String getCsrfCookie(HttpServletRequest httpReq) {
        if(httpReq.getCookies() == null){
            return null;
        }
        return Arrays.stream(httpReq.getCookies())
                .filter(c -> CSRF_COOKIE.equals(c.getName()))
                .findFirst()
                .map(c -> c.getValue())
                .orElse(null);
    }

}
