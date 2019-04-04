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

    private final Integer errorStatus;

    private final String errorDescription;

    public OAuth2ErrorException(String error, Integer errorStatus, String errorDescription) {
        super(hasText(errorDescription) ? errorDescription : error);
        this.error = error;
        this.errorStatus = errorStatus;
        this.errorDescription = errorDescription;
    }

    public String getError() {
        return error;
    }

    public Integer getErrorStatus() {
        return errorStatus;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

}
