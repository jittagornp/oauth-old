/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.resolver;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jitta
 */
public interface UserAgentTokenIdResolver {

    String resolve(HttpServletRequest httpReq);

}
