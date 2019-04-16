/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.oauth2.exception.InvalidTokenException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseInvalidTokenExceptionHandler extends ErrorResponseExceptionHandlerAdapter<InvalidTokenException> {

    @Override
    public Class<InvalidTokenException> getTypeClass() {
        return InvalidTokenException.class;
    }

    @Override
    protected ErrorResponse buildError(InvalidTokenException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        return ErrorResponse.unauthorizedClient();
    }

}
