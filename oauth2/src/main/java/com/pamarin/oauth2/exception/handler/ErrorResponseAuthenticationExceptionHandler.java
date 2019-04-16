/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.commons.exception.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseAuthenticationExceptionHandler extends ErrorResponseExceptionHandlerAdapter<AuthenticationException> {

    @Override
    public Class<AuthenticationException> getTypeClass() {
        return AuthenticationException.class;
    }

    @Override
    protected ErrorResponse buildError(AuthenticationException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        return ErrorResponse.unauthorizedClient();
    }

}
