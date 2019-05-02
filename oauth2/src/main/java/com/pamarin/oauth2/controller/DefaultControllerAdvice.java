/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pamarin.commons.validator.ValidUri;
import com.pamarin.commons.view.ModelAndViewBuilder;
import com.pamarin.oauth2.exception.handler.ErrorResponseExceptionHandlerResolver;
import com.pamarin.oauth2.exception.handler.ErrorResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author jitta
 */
@ControllerAdvice
public class DefaultControllerAdvice {

    private final ErrorResponseExceptionHandlerResolver resolver;

    private final ObjectMapper objectMapper;

    private final ValidUri.Validator uriValidator;

    @Autowired
    public DefaultControllerAdvice(ErrorResponseExceptionHandlerResolver resolver, ObjectMapper objectMapper) {
        this.resolver = resolver;
        this.objectMapper = objectMapper;
        this.uriValidator = new ValidUri.Validator();
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handle(Exception ex, HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        ErrorResponse err = resolver.resolve(ex).handle(ex, httpReq, httpResp);
        if (isRedirectError(httpReq)) {
            buildRedirectError(err, httpReq, httpResp);
        } else if (isJSONError(httpReq)) {
            buildJSONError(err, httpResp);
        } else {
            return buildModelAndViewError(err);
        }
        return null;
    }

    private void buildRedirectError(ErrorResponse err, HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String redirectUri = httpReq.getParameter("redirect_uri");
        boolean hasQueryString = redirectUri.contains("?");
        httpResp.sendRedirect(redirectUri + (hasQueryString ? "&" : "?") + err.toQueryString());
    }

    private void buildJSONError(ErrorResponse err, HttpServletResponse httpResp) throws IOException {
        String json = objectMapper.writeValueAsString(err);
        httpResp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpResp.setCharacterEncoding("utf-8");
        httpResp.setContentLength(json.getBytes().length);
        httpResp.getOutputStream().print(json);
    }

    private ModelAndView buildModelAndViewError(ErrorResponse err) throws IOException {
        String json = objectMapper.writeValueAsString(err);
        return new ModelAndViewBuilder()
                .setName("custom-error")
                .addAttribute("error", err)
                .addAttribute("errorJSON", json)
                .build();
    }

    private boolean isRedirectError(HttpServletRequest httpReq) {
        return isRedirectGet(httpReq, "/authorize")
                || isRedirectGet(httpReq, "/logout");
    }

    private boolean isRedirectGet(HttpServletRequest httpReq, String servletPath) {
        String redirectUri = httpReq.getParameter("redirect_uri");
        return "GET".equalsIgnoreCase(httpReq.getMethod())
                && servletPath.equals(httpReq.getServletPath())
                && hasText(redirectUri)
                && uriValidator.isValid(redirectUri);
    }

    private boolean isJSONError(HttpServletRequest httpReq) {
        return "/token".equals(httpReq.getServletPath())
                || "/session".equals(httpReq.getServletPath())
                || "/beat".equals(httpReq.getServletPath())
                || isPostLogout(httpReq);
    }

    private boolean isPostLogout(HttpServletRequest httpReq) {
        return "POST".equalsIgnoreCase(httpReq.getMethod())
                && "/logout".equals(httpReq.getServletPath());
    }
}