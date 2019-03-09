/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.model.OAuth2Session;

/**
 *
 * @author jitta
 */
public interface OAuth2SessionBuilderService {

    OAuth2Session build(AccessTokenVerification.Output output);

}
