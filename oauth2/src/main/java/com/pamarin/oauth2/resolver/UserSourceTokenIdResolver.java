/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.resolver;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jitta
 */
public interface UserSourceTokenIdResolver {

    String resolve(HttpServletRequest httpReq);

}
