/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception;

/**
 *
 * @author jitta
 */
public class RateLimitException extends RuntimeException {

    public RateLimitException(String message) {
        super(message);
    }

}
