/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jitta
 */
public interface HttpRequestSameOriginVerification {

    void verify(HttpServletRequest httpReq);

}
