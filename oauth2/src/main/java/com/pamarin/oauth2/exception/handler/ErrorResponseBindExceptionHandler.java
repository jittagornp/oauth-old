/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseBindExceptionHandler extends ErrorResponseExceptionHandlerAdapter<BindException>{

    @Override
    public Class<BindException> getTypeClass() {
        return BindException.class;
    }

    @Override
    protected ErrorResponse buildError(BindException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        return ErrorResponse.invalidRequest();
    }
    
}
