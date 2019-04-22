/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author jitta
 */
public interface CustomSessionConverter {

    CustomSession entriesToSession(Set<Map.Entry<String, Object>> entries);
    
}
