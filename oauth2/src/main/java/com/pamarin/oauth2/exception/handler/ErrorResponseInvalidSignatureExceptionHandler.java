/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.commons.exception.InvalidSignatureException;
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
public class ErrorResponseInvalidSignatureExceptionHandler extends ErrorResponseExceptionHandlerAdapter<InvalidSignatureException> {

    @Override
    public Class<InvalidSignatureException> getTypeClass() {
        return InvalidSignatureException.class;
    }

    @Override
    protected ErrorResponse buildError(InvalidSignatureException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        return ErrorResponse.builder()
                .error("invalid_signature")
                .errorDescription(ex.getMessage())
                .errorStatus(HttpStatus.FORBIDDEN.value())
                .build();
    }

}
