/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception;

/**
 *
 * @author jitta
 */
public class LoginOverLimitException extends RuntimeException {

    public LoginOverLimitException(String message) {
        super(message);
    }

}
