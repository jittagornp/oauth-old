/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Getter
@Setter
@Builder
public class OAuth2ErrorException extends RuntimeException {

    private String error;

    private Integer errorStatus;

    private String errorDescription;

    private String errorUri;

    private String state;

    private String errorCode;

    public OAuth2ErrorException(String error, Integer errorStatus, String errorDescription, String errorUri, String state, String errorCode) {
        super(hasText(errorDescription) ? errorDescription : error);
        this.error = error;
        this.errorStatus = errorStatus;
        this.errorDescription = errorDescription;
        this.errorUri = errorUri;
        this.state = state;
        this.errorCode = errorCode;
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
