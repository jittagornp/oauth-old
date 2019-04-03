/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.oauth2.exception.InvalidClientIdException;
import com.pamarin.oauth2.model.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseInvalidClientIdExceptionHandler extends ErrorResponseExceptionHandlerAdapter<InvalidClientIdException>{

    @Override
    public Class<InvalidClientIdException> getTypeClass() {
        return InvalidClientIdException.class;
    }

    @Override
    protected ErrorResponse buildError(InvalidClientIdException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        return ErrorResponse.invalidClient();
    }
    
}
