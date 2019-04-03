/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.oauth2.exception.InvalidClientIdAndClientSecretException;
import com.pamarin.oauth2.model.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseInvalidClientIdAndClientSecretExceptionHandler extends ErrorResponseExceptionHandlerAdapter<InvalidClientIdAndClientSecretException> {

    @Override
    public Class<InvalidClientIdAndClientSecretException> getTypeClass() {
        return InvalidClientIdAndClientSecretException.class;
    }

    @Override
    protected ErrorResponse buildError(InvalidClientIdAndClientSecretException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        return ErrorResponse.unauthorizedClient();
    }

}
