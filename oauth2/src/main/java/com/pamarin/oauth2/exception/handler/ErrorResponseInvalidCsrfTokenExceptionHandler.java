/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.commons.exception.InvalidCsrfTokenException;
import com.pamarin.oauth2.model.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseInvalidCsrfTokenExceptionHandler extends ErrorResponseExceptionHandlerAdapter<InvalidCsrfTokenException> {

    @Override
    public Class<InvalidCsrfTokenException> getTypeClass() {
        return InvalidCsrfTokenException.class;
    }

    @Override
    protected ErrorResponse buildError(InvalidCsrfTokenException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        return ErrorResponse.builder()
                .error("invalid_csrf_token")
                .errorStatus(HttpStatus.FORBIDDEN.value())
                .errorDescription(ex.getMessage())
                .build();
    }

}
