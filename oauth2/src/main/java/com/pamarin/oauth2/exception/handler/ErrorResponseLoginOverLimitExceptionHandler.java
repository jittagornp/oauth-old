/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.oauth2.exception.LoginOverLimitException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseLoginOverLimitExceptionHandler extends ErrorResponseExceptionHandlerAdapter<LoginOverLimitException> {

    @Override
    public Class<LoginOverLimitException> getTypeClass() {
        return LoginOverLimitException.class;
    }

    @Override
    protected ErrorResponse buildError(LoginOverLimitException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        return ErrorResponse.builder()
                .error("login_over_limit")
                .errorDescription(ex.getMessage())
                .errorStatus(HttpStatus.TOO_MANY_REQUESTS.value())
                .build();
    }

}
