/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository.mongodb;

import com.pamarin.oauth2.collection.UserSession;
import static java.util.Collections.emptyList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;
import com.pamarin.oauth2.repository.UserSessionRepository;

/**
 *
 * @author jitta
 */
public class MongodbUserSessionRepository implements UserSessionRepository {

    @Autowired
    private MongoOperations mongoOperations;

    private Query makeAttributeQuery(String attributeName, Object attributeValue) {
        return Query.query(Criteria.where(attributeName).is(attributeValue));
    }

    private Query makeSessionIdQuery(Object attributeValue) {
        return makeAttributeQuery("sessionId", attributeValue);
    }

    private Query makeAgentIdQuery(Object attributeValue) {
        return makeAttributeQuery("agentId", attributeValue);
    }

    private Query makeUserIdQuery(Object attributeValue) {
        return makeAttributeQuery("userId", attributeValue);
    }

    @Override
    public UserSession save(UserSession userSession) {
        mongoOperations.save(userSession);
        return userSession;
    }

    @Override
    public UserSession findBySessionId(String sessionId) {
        return mongoOperations.findOne(makeSessionIdQuery(sessionId), UserSession.class);
    }

    @Override
    public void deleteBySessionId(String sessionId) {
        mongoOperations.remove(makeSessionIdQuery(sessionId), UserSession.class);
    }

    private List<String> map(List<UserSession> userSessions) {
        return userSessions.stream().map(us -> us.getSessionId()).collect(Collectors.toList());
    }

    @Override
    public List<String> findAllSessionIdsOnSameUserAgentBySessionId(String sessionId) {
        if (!hasText(sessionId)) {
            return emptyList();
        }
        UserSession userSession = findBySessionId(sessionId);
        if (userSession == null) {
            return emptyList();
        }
        List<UserSession> userSessions = mongoOperations.find(makeAgentIdQuery(userSession.getAgentId()), UserSession.class);
        if (isEmpty(userSessions)) {
            return emptyList();
        }
        return map(userSessions);
    }

    @Override
    public List<String> findOtherSessionIdsOnSameUserAgentBySessionId(String sessionId) {
        return findAllSessionIdsOnSameUserAgentBySessionId(sessionId)
                .stream()
                .filter(s -> !sessionId.equals(s))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllSessionIdsByUserId(String userId) {
        List<UserSession> userSessions = mongoOperations.find(makeUserIdQuery(userId), UserSession.class);
        if (isEmpty(userSessions)) {
            return emptyList();
        }
        return map(userSessions);
    }
}
