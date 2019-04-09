/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

import java.net.URL;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jitta
 */
public interface HttpRequestOriginResolver {
    
    URL resolve(HttpServletRequest httpReq);
    
}
