/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import com.mongodb.DBObject;
import com.pamarin.commons.provider.DefaultHttpServletRequestProvider;
import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.commons.resolver.DefaultHttpClientIPAddressResolver;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import com.pamarin.oauth2.resolver.UserAgentTokenIdResolver;
import static com.pamarin.oauth2.session.SessionAttributeConstant.*;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;
import static org.springframework.data.mongodb.core.query.Query.query;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;

/**
 *
 * @author jitta
 */
@Slf4j
public class SessionRepositoryImpl implements SessionRepository<MapSession> {

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    private String sessionNameSpace = "user_session";

    //for mongodb
    private static final String OBJECT_ID = "_id";

    private static final String LAST_SYNCHONIZED = "lastSynchronizedTime";
    private static final String LAST_SYNCHONIZED_LOGIN = "lastSynchronizedTime:login";

    private int maxInactiveIntervalInSeconds = 1800;
    private int synchronizeTimeout = 1000 * 30;

    private final RedisOperations<Object, Object> redisOperations;
    private final MongoOperations mongoOperations;

    private final RedisSessionConverter redisConverter;
    private final MongodbSessionConverter mongodbConverter;

    private final HttpServletRequestProvider httpServletRequestProvider;

    private final UserAgentTokenIdResolver userAgentTokenIdResolver;

    private final HttpClientIPAddressResolver httpClientIPAddressResolver;

    public SessionRepositoryImpl(
            RedisOperations<Object, Object> redisOperations,
            MongoOperations mongoOperations,
            UserAgentTokenIdResolver userAgentTokenIdResolver
    ) {
        this.redisOperations = redisOperations;
        this.mongoOperations = mongoOperations;
        this.redisConverter = new DefaultRedisSessionConverter();
        this.mongodbConverter = new DefaultMongodbSessionConverter();
        this.httpServletRequestProvider = new DefaultHttpServletRequestProvider();
        this.userAgentTokenIdResolver = userAgentTokenIdResolver;
        this.httpClientIPAddressResolver = new DefaultHttpClientIPAddressResolver();
    }

    @Override
    public MapSession createSession() {
        MapSession session = new MapSession();
        session.setAttribute(SESSION_ID, session.getId());
        session.setMaxInactiveIntervalInSeconds(maxInactiveIntervalInSeconds);
        return session;
    }

    @Override
    public void save(MapSession session) {
        saveToRedis(session);
        synchronizeToMongodb(session);
    }

    @Override
    public MapSession getSession(String id) {
        MapSession session = findRedisSessionById(id);
        if (isExpired(session)) {
            session = findMongoSessionById(id);
            if (!isExpired(session)) {
                saveToRedis(session);
                return session;
            }
        }
        if (session == null) {
            return null;
        }
        if (session.isExpired()) {
            delete(session.getId());
            return null;
        }
        return session;
    }

    @Override
    public void delete(String id) {
        redisOperations.delete(getRedisKey(id));
        mongoOperations.remove(sessionIdQuery(id), sessionNameSpace);
    }

    private void saveToRedis(MapSession session) {
        redisOperations.boundHashOps(getRedisKey(session.getId()))
                .putAll(redisConverter.sessionToMap(session));
    }

    private void saveToMongodb(MapSession session) {
        additionalAttributes(session);
        DBObject dbObject = mongodbConverter.sessionToDBObject(session);
        Document document = mongoOperations.findOne(
                sessionIdQuery(session.getId()),
                Document.class,
                sessionNameSpace
        );

        if (document != null) {
            //copy _id
            dbObject.put(OBJECT_ID, document.get(OBJECT_ID));
        }
        mongoOperations.save(dbObject, sessionNameSpace);
    }

    private void synchronizeToMongodb(MapSession session) {
        long currentTime = System.currentTimeMillis();
        Long lastSyncTime = session.getAttribute(LAST_SYNCHONIZED);
        if (lastSyncTime == null || (currentTime - lastSyncTime > synchronizeTimeout)) {
            session.setAttribute(LAST_SYNCHONIZED, currentTime);
            saveToMongodb(session);
        } else {
            Long firstTimeLogin = session.getAttribute(LAST_SYNCHONIZED_LOGIN);
            if (firstTimeLogin == null) {
                boolean alreadyLogin = session.getAttribute(SPRING_SECURITY_CONTEXT) != null;
                if (alreadyLogin) {
                    session.setAttribute(LAST_SYNCHONIZED, currentTime);
                    session.setAttribute(LAST_SYNCHONIZED_LOGIN, currentTime);
                    saveToMongodb(session);
                }
            }
        }
    }

    private void additionalAttributes(MapSession session) {
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        session.setAttribute(AGENT_ID, userAgentTokenIdResolver.resolve(httpReq));
        session.setAttribute(IP_ADDRESS, httpClientIPAddressResolver.resolve(httpReq));
    }

    private boolean isExpired(MapSession session) {
        return session == null || session.isExpired();
    }

    private String getRedisKey(String sessionId) {
        return sessionNameSpace + ":" + sessionId;
    }

    private MapSession findRedisSessionById(String id) {
        Map<Object, Object> entries = redisOperations.boundHashOps(getRedisKey(id)).entries();
        return redisConverter.mapToSession(entries);
    }

    private Query sessionIdQuery(String id) {
        return query(where(SESSION_ID).is(id));
    }

    private MapSession findMongoSessionById(String id) {
        Document document = mongoOperations.findOne(sessionIdQuery(id), Document.class, sessionNameSpace);
        if (document == null) {
            return null;
        }
        return mongodbConverter.documentToSession(document);
    }

    public void setMaxInactiveIntervalInSeconds(int maxInactiveIntervalInSeconds) {
        this.maxInactiveIntervalInSeconds = maxInactiveIntervalInSeconds;
    }

    public void setSynchronizeTimeout(int synchronizeTimeout) {
        this.synchronizeTimeout = synchronizeTimeout;
    }

    public void setSessionNameSpace(String sessionNameSpace) {
        this.sessionNameSpace = sessionNameSpace;
    }

}
