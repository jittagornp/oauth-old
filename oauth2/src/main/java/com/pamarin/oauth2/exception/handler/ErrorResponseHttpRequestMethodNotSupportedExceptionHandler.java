/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.oauth2.model.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseHttpRequestMethodNotSupportedExceptionHandler extends ErrorResponseExceptionHandlerAdapter<HttpRequestMethodNotSupportedException>{

    @Override
    public Class<HttpRequestMethodNotSupportedException> getTypeClass() {
        return HttpRequestMethodNotSupportedException.class;
    }

    @Override
    protected ErrorResponse buildError(HttpRequestMethodNotSupportedException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        ErrorResponse err = ErrorResponse.invalidRequest();
        err.setErrorStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        err.setErrorDescription("Not support http '" + ex.getMethod() + "'");
        return err;
    }
    
}
