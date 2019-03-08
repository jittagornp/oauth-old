/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.resolver;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jitta
 */
public interface HttpClientIPAddressResolver {

    String resolve(HttpServletRequest httpReq);

}
