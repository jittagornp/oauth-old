/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.security;

import com.pamarin.commons.security.AuthenticityToken;
import com.pamarin.commons.util.CookieSpecBuilder;
import java.lang.reflect.Method;
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
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/16
 */
public class GetCsrfTokenIntorceptor extends HandlerInterceptorAdapter {

    private static final String CSRF_COOKIE_KEY = "csrf-token";
    private static final String CSRF_PARAM_KEY = "csrf-param";
    private static final String CSRF_PARAM_VALUE = "csrf-token";
    private static final String CSRF_ATTRIBUTE_KEY = "csrfParam";
    private static final String CSRF_ATTRIBUTE_VALUE = "csrfToken";
    private static final String CSRF_HEADER_KEY = "X-CSRF-Token";

    @NotBlank
    @Value("${server.hostUrl}")
    private String hostUrl;

    @Autowired
    private AuthenticityToken authenticityToken;

    private boolean isHandlerMethod(Object handler) {
        return handler instanceof HandlerMethod;
    }

    private boolean hasAnnotation(HandlerMethod handlerMethod, Class annotaion) {
        Method method = handlerMethod.getMethod();
        return method.getAnnotation(annotaion) != null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isHandlerMethod(handler)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (hasAnnotation(handlerMethod, GetCsrfToken.class)) {
                AuthenticityToken.RandomOutput random = authenticityToken.random();
                request.setAttribute(CSRF_PARAM_KEY, CSRF_HEADER_KEY);
                request.setAttribute(CSRF_PARAM_VALUE, random.getAuthenticityToken());
                saveToken(random, request.getSession());
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String token = (String) request.getAttribute(CSRF_PARAM_VALUE);
        if (modelAndView != null && hasText(token)) {
            modelAndView.addObject(CSRF_ATTRIBUTE_KEY, request.getAttribute(CSRF_PARAM_KEY));
            modelAndView.addObject(CSRF_ATTRIBUTE_VALUE, token);
            addTokenCookieAndHeader(token, request, response);
        }
    }

    private void saveToken(AuthenticityToken.RandomOutput random, HttpSession session) {
        session.setAttribute(CSRF_HEADER_KEY + ":" + random.getToken(), true);
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
}
