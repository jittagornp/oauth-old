/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import static com.pamarin.oauth2.session.CustomSession.Attribute.*;
import java.util.Map;
import java.util.Set;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 *
 * @author jitta
 */
public class DefaultCustomSessionConverter implements CustomSessionConverter {

    @Override
    public CustomSession entriesToSession(Set<Map.Entry<String, Object>> entries) {
        if (isEmpty(entries)) {
            return null;
        }

        CustomSession session = new CustomSession();
        entries.forEach((entry) -> {
            String key = (String) entry.getKey();
            if (SESSION_ID.equals(key)) {
                session.setId((String) entry.getValue());
                session.setSessionId((String) entry.getValue());
            } else if (CREATION_TIME.equals(key)) {
                session.setCreationTime((Long) entry.getValue());
            } else if (MAX_INACTIVE_INTERVAL.equals(key)) {
                session.setMaxInactiveIntervalInSeconds((Integer) entry.getValue());
            } else if (LAST_ACCESSED_TIME.equals(key)) {
                session.setLastAccessedTime((Long) entry.getValue());
            } else if (EXPIRATION_TIME.equals(key)) {
                session.setExpirationTime((Long) entry.getValue());
            }else if (AGENT_ID.equals(key)) {
                session.setAgentId((String) entry.getValue());
            } else if (USER_ID.equals(key)) {
                session.setUserId((String) entry.getValue());
            } else if (IP_ADDRESS.equals(key)) {
                session.setIpAddress((String) entry.getValue());
            } else if (ATTRIBUTES.equals(key)) {
                //for mongodb
            } else if (key.startsWith(ATTRIBUTES)) {
                session.setAttribute(key.substring(ATTRIBUTES.length() + 1), entry.getValue());
            }
        });
        return session;
    }

}
