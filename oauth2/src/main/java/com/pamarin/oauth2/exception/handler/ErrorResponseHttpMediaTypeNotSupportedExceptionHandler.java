/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.oauth2.model.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseHttpMediaTypeNotSupportedExceptionHandler extends ErrorResponseExceptionHandlerAdapter<HttpMediaTypeNotSupportedException>{

    @Override
    public Class<HttpMediaTypeNotSupportedException> getTypeClass() {
        return HttpMediaTypeNotSupportedException.class;
    }

    @Override
    protected ErrorResponse buildError(HttpMediaTypeNotSupportedException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        ErrorResponse err = ErrorResponse.invalidRequest();
        err.setErrorDescription("Not support media type '" + ex.getContentType() + "'");
        return err;
    }
    
}
