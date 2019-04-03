/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pamarin.commons.validator.ValidUri;
import com.pamarin.oauth2.model.ErrorResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author jitta
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorResponseExceptionHandlerResolver resolver;

    private final ObjectMapper objectMapper;

    private final ValidUri.Validator uriValidator;

    @Autowired
    public GlobalExceptionHandler(ErrorResponseExceptionHandlerResolver resolver, ObjectMapper objectMapper, ValidUri.Validator uriValidator) {
        this.resolver = resolver;
        this.objectMapper = objectMapper;
        this.uriValidator = uriValidator;

    }

    @ExceptionHandler(Exception.class)
    public void handle(Exception ex, HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        ErrorResponse err = resolver.resolve(ex).handle(ex, httpReq, httpResp);
        if (isRedirectError(httpReq)) {
            String redirectUri = httpReq.getParameter("redirect_uri");
            boolean hasQueryString = redirectUri.contains("?");
            httpResp.sendRedirect(redirectUri + (hasQueryString ? "&" : "?") + err.toQueryString());
        } else {
            String json = objectMapper.writeValueAsString(err);
            httpResp.setStatus(err.getErrorCode());
            httpResp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            httpResp.setCharacterEncoding("utf-8");
            httpResp.setContentLength(json.getBytes().length);
            httpResp.getOutputStream().print(json);
        }
    }

    private boolean isRedirectError(HttpServletRequest httpReq) {
        String redirectUri = httpReq.getParameter("redirect_uri");
        return "GET".equalsIgnoreCase(httpReq.getMethod())
                && "/authorize".equals(httpReq.getServletPath())
                && hasText(redirectUri)
                && uriValidator.isValid(redirectUri);
    }
}
