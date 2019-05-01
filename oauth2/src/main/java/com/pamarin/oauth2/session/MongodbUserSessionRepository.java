/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import static com.pamarin.commons.util.DateConverterUtils.convert2Timestamp;
import static com.pamarin.oauth2.session.CustomSession.Attribute.*;
import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 *
 * @author jitta
 */
public class MongodbUserSessionRepository implements UserSessionRepository {

    @Autowired
    private MongoOperations mongoOperations;

    private Query attributeQuery(String name, Object value) {
        return query(where(name).is(value));
    }

    private Query sessionIdQuery(Object value) {
        return attributeQuery(SESSION_ID, value);
    }

    private Query agentIdQuery(Object value) {
        return attributeQuery(AGENT_ID, value);
    }

    private Query userIdQuery(Object value) {
        return attributeQuery(USER_ID, value);
    }

    public UserSession findBySessionId(String sessionId) {
        return mongoOperations.findOne(sessionIdQuery(sessionId), UserSession.class);
    }

    private List<String> map(List<UserSession> session) {
        return session.stream().map(UserSession::getSessionId).collect(toList());
    }

    @Override
    public List<String> findAllSessionIdsOnSameUserAgentBySessionId(String sessionId) {
        if (!hasText(sessionId)) {
            return emptyList();
        }
        UserSession session = findBySessionId(sessionId);
        if (session == null) {
            return emptyList();
        }
        List<UserSession> sessions = mongoOperations.find(agentIdQuery(session.getAgentId()), UserSession.class);
        if (isEmpty(sessions)) {
            return emptyList();
        }
        return map(sessions);
    }

    @Override
    public List<String> findOtherSessionIdsOnSameUserAgentBySessionId(String sessionId) {
        return findAllSessionIdsOnSameUserAgentBySessionId(sessionId)
                .stream()
                .filter(s -> !sessionId.equals(s))
                .collect(toList());
    }

    @Override
    public List<String> findAllSessionIdsByUserId(String userId) {
        List<UserSession> sessions = mongoOperations.find(userIdQuery(userId), UserSession.class);
        if (isEmpty(sessions)) {
            return emptyList();
        }
        return map(sessions);
    }

    @Override
    public List<String> findExpiredSessions() {
        Query query = query(where(EXPIRATION_TIME).lt(convert2Timestamp(now())));
        List<UserSession> sessions = mongoOperations.find(query, UserSession.class);
        if (isEmpty(sessions)) {
            return emptyList();
        }
        return sessions.stream()
                .map(UserSession::getSessionId)
                .collect(toList());
    }
}
