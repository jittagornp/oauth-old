/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import java.util.List;

/**
 *
 * @author jitta
 */
public interface RevokeSessionService {

    void revoke(String sessionId);
    
    void revoke(List<String> sessionIds);

}
