/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import static com.pamarin.oauth2.session.SessionAttributeConstant.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.session.MapSession;

/**
 *
 * @author jitta
 */
public class DefaultSessionConverter implements SessionConverter {

    private boolean ignoreAttribute(String attrName) {
        return AGENT_ID.equals(attrName)
                || USER_ID.equals(attrName)
                || IP_ADDRESS.equals(attrName)
                || SESSION_ID.equals(attrName);
    }

    @Override
    public Map<String, Object> getSessionAttributes(MapSession session) {
        Map<String, Object> attributes = new HashMap<>();
        session.getAttributeNames().forEach(attrName -> {
            if (!ignoreAttribute(attrName)) {
                attributes.put(attrName, session.getAttribute(attrName));
            }
        });
        return attributes;
    }

    @Override
    public MapSession entriesToSession(Set<Map.Entry<String, Object>> entries) {
        if (entries == null) {
            return null;
        }

        MapSession session = new MapSession();
        entries.forEach((entry) -> {
            String key = (String) entry.getKey();
            if (SESSION_ID.equals(key)) {
                session.setId((String) entry.getValue());
            } else if (CREATION_TIME.equals(key)) {
                session.setCreationTime((Long) entry.getValue());
            } else if (MAX_INACTIVE_INTERVAL.equals(key)) {
                session.setMaxInactiveIntervalInSeconds((Integer) entry.getValue());
            } else if (LAST_ACCESSED_TIME.equals(key)) {
                session.setLastAccessedTime((Long) entry.getValue());
            } else if (AGENT_ID.equals(key)) {
                session.setAttribute(AGENT_ID, (String) entry.getValue());
            } else if (USER_ID.equals(key)) {
                session.setAttribute(USER_ID, (String) entry.getValue());
            } else if (IP_ADDRESS.equals(key)) {
                session.setAttribute(IP_ADDRESS, (String) entry.getValue());
            } else if (ATTRIBUTES.equals(key)) {
                //for mongodb
            } else if (key.startsWith(ATTRIBUTES)) {
                session.setAttribute(key.substring(ATTRIBUTES.length() + 1), entry.getValue());
            }
        });
        return session;
    }

}
