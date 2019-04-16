/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.oauth2.exception.UnauthorizedClientException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseUnauthorizedClientExceptionHandler extends ErrorResponseExceptionHandlerAdapter<UnauthorizedClientException>{

    @Override
    public Class<UnauthorizedClientException> getTypeClass() {
        return UnauthorizedClientException.class;
    }

    @Override
    protected ErrorResponse buildError(UnauthorizedClientException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        return ErrorResponse.unauthorizedClient();
    }
    
}
