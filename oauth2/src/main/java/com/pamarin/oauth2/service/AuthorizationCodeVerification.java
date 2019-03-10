/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.domain.OAuth2AuthorizationCode;

/**
 *
 * @author jitta
 */
public interface AuthorizationCodeVerification {

    OAuth2AuthorizationCode verify(String code);

}
