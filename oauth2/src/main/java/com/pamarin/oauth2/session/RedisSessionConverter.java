/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import java.util.Map;

/**
 *
 * @author jitta
 */
public interface RedisSessionConverter {

    Map<String, Object> sessionToMap(UserSession session);

    UserSession mapToSession(Map<Object, Object> map);

}
