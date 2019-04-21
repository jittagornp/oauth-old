/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import java.util.Map;
import org.springframework.session.MapSession;

/**
 *
 * @author jitta
 */
public interface RedisSessionConverter {

    Map<String, Object> sessionToMap(MapSession session);

    MapSession mapToSession(Map<Object, Object> map);

}
