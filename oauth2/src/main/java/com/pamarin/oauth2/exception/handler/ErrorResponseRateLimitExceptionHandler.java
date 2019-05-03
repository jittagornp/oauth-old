/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.oauth2.exception.RateLimitException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseRateLimitExceptionHandler extends ErrorResponseExceptionHandlerAdapter<RateLimitException> {

    @Override
    public Class<RateLimitException> getTypeClass() {
        return RateLimitException.class;
    }

    @Override
    protected ErrorResponse buildError(RateLimitException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        return ErrorResponse.builder()
                .error("rate_limit")
                .errorDescription(ex.getMessage())
                .errorStatus(HttpStatus.TOO_MANY_REQUESTS.value())
                .build();
    }

}
