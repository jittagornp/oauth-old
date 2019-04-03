/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

/**
 *
 * @author jitta
 */
public interface ErrorResponseExceptionHandlerResolver {

    ErrorResponseExceptionHandler resolve(Exception ex);

}
