/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.util.CookieSpecBuilder;
import com.pamarin.commons.exception.InvalidCsrfTokenException;
import com.pamarin.commons.resolver.DefaultHttpCookieResolver;
import com.pamarin.commons.resolver.HttpCookieResolver;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/17
 *
 * Implement follow
 * https://github.com/OWASP/CheatSheetSeries/blob/master/cheatsheets/Cross-Site_Request_Forgery_Prevention_Cheat_Sheet.md
 */
public class CsrfInterceptor extends HandlerInterceptorAdapter {

    private static final String CSRF_COOKIE_KEY = "csrf-token";
    private static final String CSRF_PARAM_VALUE = "csrf-token";
    private static final String CSRF_ATTRIBUTE_KEY = "csrfParam";
    private static final String CSRF_ATTRIBUTE_VALUE = "csrfToken";
    private static final String CSRF_HEADER_KEY = "X-CSRF-Token";

    private final String hostUrl;
    private AuthenticityToken authenticityToken;
    private final HttpCookieResolver cookieResolver;
    private final HttpRequestSameOriginVerification sameOriginVerification;
    private List<String> ignorePaths;

    public CsrfInterceptor(String hostUrl, int tokenSize) {
        this.hostUrl = hostUrl;
        this.sameOriginVerification = new DefaultHttpRequestSameOriginVerification(hostUrl);
        this.authenticityToken = new DefaultAuthenticityToken(tokenSize);
        this.cookieResolver = new DefaultHttpCookieResolver(CSRF_PARAM_VALUE);
    }

    public void setAuthenticityToken(AuthenticityToken authenticityToken) {
        this.authenticityToken = authenticityToken;
    }

    public void setIgnorePaths(String... ignorePaths) {
        this.ignorePaths = Arrays.asList(ignorePaths);
    }

    private List<String> getIgnorePaths() {
        if (ignorePaths == null) {
            ignorePaths = new ArrayList<>();
        }
        return ignorePaths;
    }

    private boolean isIgnorePath(String path) {
        return getIgnorePaths().contains(path);
    }

    private boolean checkCsrf(String httpMethod) {
        Pattern pattern = Pattern.compile("[POST|PUT|DELETE|PATCH]+", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(httpMethod).matches();
    }

    @Override
    public boolean preHandle(HttpServletRequest httpReq, HttpServletResponse httpResp, Object handler) throws Exception {
        if (isIgnorePath(httpReq.getServletPath()) || !checkCsrf(httpReq.getMethod())) {
            return true;
        }

        //1. Verify Same Origin
        sameOriginVerification.verify(httpReq);

        //2. Check Double Submit Cookie 
        String csrfToken = getCsrfToken(httpReq);
        String csrfCookie = cookieResolver.resolve(httpReq);
        doubleSubmitCookie(csrfToken, csrfCookie);

        //3. Verify CSRF token in Session
        synchronizeSession(csrfToken, httpReq);

        return true;
    }

    private void synchronizeSession(String csrfToken, HttpServletRequest httpReq) {
        String token = authenticityToken.decode(csrfToken);
        if (!hasText(token)) {
            throw new InvalidCsrfTokenException("Can't decode csrf token");
        }

        String attribute = getCsrfSessionKey(httpReq.getServletPath());
        if (!Objects.equals(httpReq.getSession().getAttribute(attribute), token)) {
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

    private String getCsrfToken(HttpServletRequest httpReq) {
        String csrfToken = httpReq.getParameter(CSRF_HEADER_KEY);
        if (!hasText(csrfToken)) {
            csrfToken = httpReq.getHeader(CSRF_HEADER_KEY);
        }
        return csrfToken;
    }

    private boolean isHandlerMethod(Object handler) {
        return handler instanceof HandlerMethod;
    }

    private boolean hasAnnotation(HandlerMethod handlerMethod, Class annotaion) {
        Method method = handlerMethod.getMethod();
        return method.getAnnotation(annotaion) != null;
    }

    @Override
    public void postHandle(HttpServletRequest httpReq, HttpServletResponse httpResp, Object handler, ModelAndView modelAndView) throws Exception {
        if (isOK(httpResp) && modelAndView != null && isHandlerMethod(handler)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (hasAnnotation(handlerMethod, GetCsrfToken.class)) {
                AuthenticityToken.RandomOutput random = authenticityToken.random();
                modelAndView.addObject(CSRF_ATTRIBUTE_KEY, CSRF_HEADER_KEY);
                modelAndView.addObject(CSRF_ATTRIBUTE_VALUE, random.getAuthenticityToken());
                saveToken(random, httpReq);
                addTokenCookieAndHeader(random.getAuthenticityToken(), httpReq, httpResp);
            }
        }
    }

    private boolean isOK(HttpServletResponse httpResp) {
        return httpResp.getStatus() == HttpServletResponse.SC_OK;
    }

    private void saveToken(AuthenticityToken.RandomOutput random, HttpServletRequest request) {
        String attribute = getCsrfSessionKey(request.getServletPath());
        String value = random.getToken();
        request.getSession().setAttribute(attribute, value);
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

    private String getCsrfSessionKey(String path) {
        return CSRF_HEADER_KEY + ":" + path;
    }

}
