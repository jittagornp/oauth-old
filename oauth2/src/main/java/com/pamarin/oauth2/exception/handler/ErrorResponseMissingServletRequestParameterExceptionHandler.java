/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseMissingServletRequestParameterExceptionHandler extends ErrorResponseExceptionHandlerAdapter<MissingServletRequestParameterException> {

    @Override
    public Class<MissingServletRequestParameterException> getTypeClass() {
        return MissingServletRequestParameterException.class;
    }

    @Override
    protected ErrorResponse buildError(MissingServletRequestParameterException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        ErrorResponse err = ErrorResponse.invalidRequest();
        err.setErrorDescription("Require parameter " + ex.getParameterName() + " (" + ex.getParameterType() + ")");
        return err;
    }

}
