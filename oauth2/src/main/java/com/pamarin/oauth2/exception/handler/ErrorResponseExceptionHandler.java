/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jitta
 * @param <E>
 */
public interface ErrorResponseExceptionHandler<E extends Exception> {
   
    Class<E> getTypeClass();
    
    ErrorResponse handle(E ex, HttpServletRequest httpReq, HttpServletResponse httpResp);
    
}
