/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import com.pamarin.commons.resolver.DefaultPrincipalNameResolver;
import com.pamarin.commons.resolver.PrincipalNameResolver;
import static com.pamarin.oauth2.session.SessionAttributeConstant.*;
import java.util.HashMap;
import java.util.Map;
import static java.util.stream.Collectors.toSet;
import org.springframework.session.MapSession;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 *
 * @author jitta
 */
public class DefaultRedisSessionConverter implements RedisSessionConverter {

    private final PrincipalNameResolver principalNameResolver;
    private final SessionConverter sessionConverter;

    public DefaultRedisSessionConverter() {
        this.principalNameResolver = new DefaultPrincipalNameResolver();
        this.sessionConverter = new DefaultSessionConverter();
    }

    @Override
    public Map<String, Object> sessionToMap(MapSession session) {
        Map<String, Object> map = new HashMap<>();
        map.put(SESSION_ID, session.getId());
        map.put(CREATION_TIME, session.getCreationTime());
        map.put(MAX_INACTIVE_INTERVAL, session.getMaxInactiveIntervalInSeconds());
        map.put(LAST_ACCESSED_TIME, session.getLastAccessedTime());
        map.put(USER_ID, principalNameResolver.resolve(session));
        sessionConverter.getSessionAttributes(session)
                .entrySet()
                .forEach(attribute -> {
                    map.put(ATTRIBUTES + ":" + attribute.getKey(), attribute.getValue());
                });
        return map;
    }

    @Override
    public MapSession mapToSession(Map<Object, Object> map) {
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
