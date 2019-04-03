/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.oauth2.model.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseRootExceptionHandler extends ErrorResponseExceptionHandlerAdapter<Exception>{

    @Override
    public Class<Exception> getTypeClass() {
        return Exception.class;
    }

    @Override
    protected ErrorResponse buildError(Exception ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        return ErrorResponse.serverError();
    }
    
}
