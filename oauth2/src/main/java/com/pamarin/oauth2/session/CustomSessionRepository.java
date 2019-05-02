/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import com.mongodb.DBObject;
import com.pamarin.commons.provider.DefaultHttpServletRequestProvider;
import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.commons.resolver.DefaultHttpClientIPAddressResolver;
import com.pamarin.commons.resolver.DefaultPrincipalNameResolver;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import com.pamarin.commons.resolver.PrincipalNameResolver;
import static com.pamarin.commons.util.DateConverterUtils.convert2Timestamp;
import com.pamarin.oauth2.resolver.UserAgentTokenIdResolver;
import static com.pamarin.oauth2.session.CustomSession.Attribute.*;
import static java.time.LocalDateTime.now;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;
import static org.springframework.data.mongodb.core.query.Query.query;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.session.SessionRepository;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Slf4j
public class CustomSessionRepository implements SessionRepository<CustomSession> {

    private String sessionNameSpace = "user_session";

    //for mongodb
    private static final String OBJECT_ID = "_id";

    private static final String LAST_SYNCHONIZED = "lastSynchronizedTime";

    private final int ANONYMOUS_MAX_INACTIVE_INTERVAL = 60;//1 minute
    private int maxInactiveIntervalInSeconds = 1800; //30 minutes
    private int synchronizeTimeout = 1000 * 30; //30 seconds

    private final RedisOperations<Object, Object> redisOperations;
    private final MongoOperations mongoOperations;

    private final RedisSessionConverter redisConverter;
    private final MongodbSessionConverter mongodbConverter;

    private final HttpServletRequestProvider httpServletRequestProvider;

    private final UserAgentTokenIdResolver userAgentTokenIdResolver;

    private final HttpClientIPAddressResolver httpClientIPAddressResolver;

    private final PrincipalNameResolver principalNameResolver;

    public CustomSessionRepository(
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
        this.principalNameResolver = new DefaultPrincipalNameResolver();
    }

    @Override
    public CustomSession createSession() {
        CustomSession session = new CustomSession(ANONYMOUS_MAX_INACTIVE_INTERVAL);
        log.debug("create \"{}\".\"{}\"", sessionNameSpace, session.getId());
        return session;
    }

    @Override
    public void save(CustomSession session) {
        log.debug("save \"{}\".\"{}\"", sessionNameSpace, session.getId());
        String userId = principalNameResolver.resolve(session);
        boolean anonymousUser = isAnonymousUser(userId);
        int interval = anonymousUser ? ANONYMOUS_MAX_INACTIVE_INTERVAL : maxInactiveIntervalInSeconds;
        session.setMaxInactiveIntervalInSeconds(interval);
        session.setLastAccessedTime(convert2Timestamp(now()));
        session.setExpirationTime(session.getLastAccessedTime() + TimeUnit.SECONDS.toMillis(interval));
        session.setUserId(userId);

        saveToRedis(session);

        if (!anonymousUser) {
            synchronizeToMongodb(session);
        }
    }

    @Override
    public CustomSession getSession(String id) {
        log.debug("get \"{}\".\"{}\"", sessionNameSpace, id);
        CustomSession session = findRedisSessionById(id);
        if (isExpired(session)) {
            session = findMongoSessionById(id);
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
        log.debug("delete \"{}\".\"{}\"", sessionNameSpace, id);
        redisOperations.delete(getRedisKey(id));
        mongoOperations.remove(sessionIdQuery(id), sessionNameSpace);
    }

    private boolean isAnonymousUser(String userId) {
        if (!hasText(userId)) {
            return true;
        }
        return "anonymousUser".equals(userId);
    }

    private void saveToRedis(CustomSession session) {
        redisOperations.boundHashOps(getRedisKey(session.getId()))
                .putAll(redisConverter.sessionToMap(session));
    }

    private void saveToMongodb(CustomSession session) {
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

    private void synchronizeToMongodb(CustomSession session) {
        long now = convert2Timestamp(now());
        Long lastSyncTime = session.getAttribute(LAST_SYNCHONIZED);
        if (lastSyncTime == null || (now - lastSyncTime > synchronizeTimeout)) {
            session.setAttribute(LAST_SYNCHONIZED, now);
            saveToMongodb(session);
        }
    }

    private void additionalAttributes(CustomSession session) {
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        String agentId = userAgentTokenIdResolver.resolve(httpReq);
        if (hasText(agentId)) {
            session.setAgentId(agentId);
        }
        String ipAddress = httpClientIPAddressResolver.resolve(httpReq);
        if (hasText(ipAddress)) {
            session.setIpAddress(ipAddress);
        }
    }

    private boolean isExpired(CustomSession session) {
        return session == null || session.isExpired();
    }

    private String getRedisKey(String sessionId) {
        return sessionNameSpace + ":" + sessionId;
    }

    private CustomSession findRedisSessionById(String id) {
        Map<Object, Object> entries = redisOperations.boundHashOps(getRedisKey(id)).entries();
        return redisConverter.mapToSession(entries);
    }

    private Query sessionIdQuery(String id) {
        return query(where(SESSION_ID).is(id));
    }

    private CustomSession findMongoSessionById(String id) {
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
