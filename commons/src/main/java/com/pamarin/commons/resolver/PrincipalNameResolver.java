/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

import org.springframework.session.Session;

/**
 *
 * @author jitta
 */
public interface PrincipalNameResolver {
    
    String resolve(Session session);
    
}
