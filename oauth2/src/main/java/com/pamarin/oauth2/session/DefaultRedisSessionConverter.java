/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import com.pamarin.commons.resolver.DefaultPrincipalNameResolver;
import com.pamarin.commons.resolver.PrincipalNameResolver;
import static com.pamarin.oauth2.session.CustomSession.Attribute.*;
import java.util.HashMap;
import java.util.Map;
import static java.util.stream.Collectors.toSet;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 *
 * @author jitta
 */
public class DefaultRedisSessionConverter implements RedisSessionConverter {

    private final PrincipalNameResolver principalNameResolver;
    private final CustomSessionConverter sessionConverter;

    public DefaultRedisSessionConverter() {
        this.principalNameResolver = new DefaultPrincipalNameResolver();
        this.sessionConverter = new DefaultCustomSessionConverter();
    }

    @Override
    public Map<String, Object> sessionToMap(CustomSession session) {
        Map<String, Object> map = new HashMap<>();
        map.put(SESSION_ID, session.getId());
        map.put(CREATION_TIME, session.getCreationTime());
        map.put(MAX_INACTIVE_INTERVAL, session.getMaxInactiveIntervalInSeconds());
        map.put(LAST_ACCESSED_TIME, session.getLastAccessedTime());
        map.put(EXPIRATION_TIME, session.getExpirationTime());
        map.put(USER_ID, principalNameResolver.resolve(session));
        session.getAttributes()
                .entrySet()
                .forEach(attribute -> {
                    map.put(ATTRIBUTES + ":" + attribute.getKey(), attribute.getValue());
                });
        return map;
    }

    @Override
    public CustomSession mapToSession(Map<Object, Object> map) {
        if (isEmpty(map)) {
            return null;
        }
        return sessionConverter.entriesToSession(
                map.entrySet()
                        .stream()
                        .map(this::convertEntry)
                        .collect(toSet())
        );
    }

    private Map.Entry<String, Object> convertEntry(Map.Entry<Object, Object> entry) {
        return new Map.Entry<String, Object>() {
            @Override
            public String getKey() {
                return (String) entry.getKey();
            }

            @Override
            public Object getValue() {
                return entry.getValue();
            }

            @Override
            public Object setValue(Object value) {
                entry.setValue(value);
                return value;
            }
        };
    }

}
