/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.oauth2.model.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jitta
 * @param <E>
 */
public abstract class ErrorResponseExceptionHandlerAdapter<E extends Exception> implements ErrorResponseExceptionHandler<E> {

    protected abstract ErrorResponse buildError(E ex, HttpServletRequest httpReq, HttpServletResponse httpResp);

    private ErrorResponse additional(ErrorResponse err, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        err.setState(httpReq.getParameter("state"));
        httpResp.setStatus(err.getErrorStatus() == null ? HttpServletResponse.SC_INTERNAL_SERVER_ERROR : err.getErrorStatus());
        return err;
    }

    @Override
    public ErrorResponse handle(E ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        return additional(buildError(ex, httpReq, httpResp), httpReq, httpResp);
    }

}
