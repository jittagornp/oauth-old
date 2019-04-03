/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.oauth2.exception.InvalidRedirectUriException;
import com.pamarin.oauth2.model.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseInvalidRedirectUriExceptionHandler extends ErrorResponseExceptionHandlerAdapter<InvalidRedirectUriException> {

    @Override
    public Class<InvalidRedirectUriException> getTypeClass() {
        return InvalidRedirectUriException.class;
    }

    @Override
    protected ErrorResponse buildError(InvalidRedirectUriException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        ErrorResponse err = ErrorResponse.invalidRequest();
        err.setErrorDescription("Invalid format '" + ex.getRedirectUri() + "'");
        return err;
    }

}
