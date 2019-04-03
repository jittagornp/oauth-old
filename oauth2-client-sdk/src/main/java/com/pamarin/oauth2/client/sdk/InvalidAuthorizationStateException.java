/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

/**
 *
 * @author jitta
 */
public class InvalidAuthorizationStateException extends RuntimeException {

    private final String state;

    public InvalidAuthorizationStateException(String state) {
        super("Invalid Authorization State : " + state);
        this.state = state;
    }

    public String getState() {
        return state;
    }

}
