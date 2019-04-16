/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.commons.generator.UUIDGenerator;
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

    private final UUIDGenerator uuidGenerator;

    @Autowired
    public ErrorResponseRootExceptionHandler(UUIDGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;
    }

    @Override
    public Class<Exception> getTypeClass() {
        return Exception.class;
    }

    @Override
    protected ErrorResponse buildError(Exception ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        String code = random();
        LOG.error("error code => {}", code);
        ErrorResponse err = ErrorResponse.serverError();
        err.setErrorCode(code);
        return err;
    }

    private String random() {
        return uuidGenerator.generate()
                .replace("-", "")
                .toUpperCase()
                .substring(0, 8);
    }
}
