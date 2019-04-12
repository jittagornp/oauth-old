/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.commons.exception.InvalidSameOriginException;
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
public class ErrorResponseInvalidSameOriginExceptionHandler extends ErrorResponseExceptionHandlerAdapter<InvalidSameOriginException> {

    @Override
    public Class<InvalidSameOriginException> getTypeClass() {
        return InvalidSameOriginException.class;
    }

    @Override
    protected ErrorResponse buildError(InvalidSameOriginException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        return ErrorResponse.builder()
                .error("invalid_same_origin")
                .errorStatus(HttpStatus.FORBIDDEN.value())
                .errorDescription(ex.getMessage())
                .build();
    }

}
