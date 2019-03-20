/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.converter;

import com.pamarin.commons.resolver.UserAgent;
import com.pamarin.oauth2.domain.UserAgentEntity;

/**
 *
 * @author jitta
 */
public interface UserAgentConverter {
    
    UserAgentEntity convert(UserAgent userAgent);
    
}
