/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
public class OAuth2ErrorException extends RuntimeException {

    private final String error;

    private final String errorCode;

    private final String errorDescription;

    public OAuth2ErrorException(String error, String errorCode, String errorDescription) {
        super(hasText(errorDescription) ? errorDescription : error);
        this.error = error;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public String getError() {
        return error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

}
