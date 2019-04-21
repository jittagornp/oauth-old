/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import java.util.Map;
import java.util.Set;
import org.springframework.session.MapSession;

/**
 *
 * @author jitta
 */
public interface SessionConverter {
    
    Map<String, Object> getSessionAttributes(MapSession session);
    
    MapSession entriesToSession(Set<Map.Entry<String, Object>> entries);
    
}
