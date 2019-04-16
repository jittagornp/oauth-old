/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.oauth2.exception.InvalidScopeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseInvalidScopeExceptionHandler extends ErrorResponseExceptionHandlerAdapter<InvalidScopeException>{

    @Override
    public Class<InvalidScopeException> getTypeClass() {
        return InvalidScopeException.class;
    }

    @Override
    protected ErrorResponse buildError(InvalidScopeException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        return ErrorResponse.invalidScope();
    }
    
}
