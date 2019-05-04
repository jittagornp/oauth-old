/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.ratelimit;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jitta
 */
public interface LoginRateLimitService {

    void limit(HttpServletRequest httpReq);

    void limit(String username);

}
