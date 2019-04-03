/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.oauth2.exception.InvalidResponseTypeException;
import com.pamarin.oauth2.model.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseInvalidResponseTypeExceptionHandler extends ErrorResponseExceptionHandlerAdapter<InvalidResponseTypeException>{

    @Override
    public Class<InvalidResponseTypeException> getTypeClass() {
        return InvalidResponseTypeException.class;
    }

    @Override
    protected ErrorResponse buildError(InvalidResponseTypeException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        return ErrorResponse.unsupportedResponseType();
    }
    
}
