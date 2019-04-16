/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.commons.generator.ErrorCodeGenerator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseRootExceptionHandler extends ErrorResponseExceptionHandlerAdapter<Exception> {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorResponseRootExceptionHandler.class);

    private final ErrorCodeGenerator errorCodeGenerator;

    @Autowired
    public ErrorResponseRootExceptionHandler(ErrorCodeGenerator errorCodeGenerator) {
        this.errorCodeGenerator = errorCodeGenerator;
    }

    @Override
    public Class<Exception> getTypeClass() {
        return Exception.class;
    }

    @Override
    protected ErrorResponse buildError(Exception ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        String code = errorCodeGenerator.generate();
        LOG.error("error code => {}", code);
        ErrorResponse err = ErrorResponse.serverError();
        err.setErrorCode(code);
        return err;
    }
}
