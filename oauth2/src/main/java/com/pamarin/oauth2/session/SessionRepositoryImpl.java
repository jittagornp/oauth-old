/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import com.pamarin.oauth2.resolver.UserAgentTokenIdResolver;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.Binary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;

/**
 *
 * @author jitta
 */
@Slf4j
public class SessionRepositoryImpl implements SessionRepository<MapSession> {

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    private static final String COLLECTION_NAME = "user_session";

    private static final String OBJECT_ID = "_id";
    private static final String SESSION_ID = "sessionId";
    private static final String CREATION_TIME = "creationTime";
    private static final String LAST_ACCESSED_TIME = "lastAccessedTime";
    private static final String MAX_INACTIVE_INTERVAL = "maxInactiveInterval";
    private static final String AGENT_ID = "agentId";
    private static final String USER_ID = "userId";
    private static final String IP_ADDRESS = "ipAddress";
    private static final String ATTRIBUTES = "attrs";
    //
    private static final String LAST_ACCESSED_TIME_ATTR = "lastAccessedTime";
    private static final String LAST_ACCESSED_TIME_WITH_LOGIN_ATTR = "lastAccessedTimeWithLogin";

    private final int maxInactiveIntervalInSeconds = 1800;
    private final int synchronizeTimeout = 1000 * 30;

    private final RedisOperations<Object, Object> redisOperations;
    private final MongoOperations mongoOperations;
    private final Converter<Object, byte[]> serializer;
    private final Converter<byte[], Object> deserializer;

    private final AuthenticationParser authenticationParser;

    private final HttpServletRequestProvider httpServletRequestProvider;

    private final UserAgentTokenIdResolver userAgentTokenIdResolver;

    private final HttpClientIPAddressResolver httpClientIPAddressResolver;

    public SessionRepositoryImpl(
            RedisOperations<Object, Object> redisOperations,
            MongoOperations mongoOperations,
            HttpServletRequestProvider httpServletRequestProvider,
            UserAgentTokenIdResolver userAgentTokenIdResolver,
            HttpClientIPAddressResolver httpClientIPAddressResolver
    ) {
        this.redisOperations = redisOperations;
        this.mongoOperations = mongoOperations;
        this.httpServletRequestProvider = httpServletRequestProvider;
        this.userAgentTokenIdResolver = userAgentTokenIdResolver;
        this.httpClientIPAddressResolver = httpClientIPAddressResolver;
        this.serializer = new SerializingConverter();
        this.deserializer = new DeserializingConverter();
        this.authenticationParser = new AuthenticationParser();
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

    private void saveToRedis(MapSession session) {
        redisOperations.boundHashOps(getRedisKey(session.getId())).putAll(toRedisMap(session));
    }

    private void saveToMongodb(MapSession session) {
        DBObject obj = toDBObject(session);
        Query query = Query.query(Criteria.where(SESSION_ID).is(session.getId()));
        Document db = mongoOperations.findOne(query, Document.class, COLLECTION_NAME);
        if (db != null) {
            obj.put(OBJECT_ID, db.get(OBJECT_ID));
        }
        mongoOperations.save(obj, COLLECTION_NAME);
    }

    private void synchronizeToMongodb(MapSession session) {
        long currentTime = System.currentTimeMillis();
        Long lastAcccessedTime = session.getAttribute(LAST_ACCESSED_TIME_ATTR);
        if (lastAcccessedTime == null || (currentTime - lastAcccessedTime > synchronizeTimeout)) {
            session.setAttribute(LAST_ACCESSED_TIME_ATTR, currentTime);
            saveToMongodb(session);
        } else {
            Long firstTimeWithLogin = session.getAttribute(LAST_ACCESSED_TIME_WITH_LOGIN_ATTR);
            if (firstTimeWithLogin == null) {
                boolean alreadyLogin = session.getAttribute(SPRING_SECURITY_CONTEXT) != null;
                if (alreadyLogin) {
                    session.setAttribute(LAST_ACCESSED_TIME_ATTR, currentTime);
                    session.setAttribute(LAST_ACCESSED_TIME_WITH_LOGIN_ATTR, currentTime);
                    saveToMongodb(session);
                }
            }
        }
    }

    @Override
    public MapSession getSession(String id) {
        MapSession session = findRedisSessionById(id);
        if (session == null || session.isExpired()) {
            log.debug("redis session expires => {}", session);
            session = findMongoSessionById(id);
            if (session != null) {
                log.debug("find session in mongodb => {}", session.getId());
            }
        }
        printSeessionAttributes(session);
        return session;
    }

    @Override
    public void delete(String id) {

    }

    private String getRedisKey(String sessionId) {
        return COLLECTION_NAME + ":" + sessionId;
    }

    private MapSession findRedisSessionById(String id) {
        log.debug("find session in redis => {}", id);
        Map<Object, Object> entries = redisOperations.boundHashOps(getRedisKey(id)).entries();
        if (entries == null || entries.isEmpty()) {
            return null;
        }
        return toMapSession(
                entries.entrySet()
                        .stream()
                        .map(this::convertEntry)
                        .collect(toSet())
        );
    }

    private MapSession toMapSession(Set<Entry<String, Object>> entries) {
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

    private Map<String, Object> toRedisMap(MapSession session) {
        Map<String, Object> map = new HashMap<>();
        map.put(SESSION_ID, session.getId());
        map.put(CREATION_TIME, session.getCreationTime());
        map.put(MAX_INACTIVE_INTERVAL, session.getMaxInactiveIntervalInSeconds());
        map.put(LAST_ACCESSED_TIME, session.getLastAccessedTime());
        map.put(USER_ID, resolveUserId(session));
        getAttributeMap(session).entrySet().forEach(attribute -> {
            map.put(ATTRIBUTES + ":" + attribute.getKey(), attribute.getValue());
        });
        map.entrySet().forEach(entry -> {
            log.debug("save session attribute => {} : {}", entry.getKey(), entry.getValue());
        });
        return map;
    }

    private DBObject toDBObject(MapSession session) {
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        BasicDBObject obj = new BasicDBObject();
        obj.put(SESSION_ID, session.getId());
        obj.put(CREATION_TIME, session.getCreationTime());
        obj.put(MAX_INACTIVE_INTERVAL, session.getMaxInactiveIntervalInSeconds());
        obj.put(LAST_ACCESSED_TIME, session.getLastAccessedTime());
        obj.put(AGENT_ID, userAgentTokenIdResolver.resolve(httpReq));
        obj.put(USER_ID, session.getAttribute(USER_ID));
        obj.put(IP_ADDRESS, httpClientIPAddressResolver.resolve(httpReq));
        obj.put(ATTRIBUTES, serializeAttributes(session));
        return obj;
    }

    private Map<String, Object> getAttributeMap(MapSession session) {
        Map<String, Object> attributes = new HashMap<>();
        session.getAttributeNames().forEach(attrName -> {
            boolean ignore = AGENT_ID.equals(attrName)
                    || USER_ID.equals(attrName)
                    || IP_ADDRESS.equals(attrName)
                    || SESSION_ID.equals(attrName);
            if (!ignore) {
                attributes.put(attrName, session.getAttribute(attrName));
            }
        });
        return attributes;
    }

    private byte[] serializeAttributes(MapSession session) {
        return this.serializer.convert(getAttributeMap(session));
    }

    private MapSession findMongoSessionById(String id) {
        Query query = Query.query(Criteria.where(SESSION_ID).is(id));
        Document document = mongoOperations.findOne(query, Document.class, COLLECTION_NAME);
        if (document == null) {
            return null;
        }
        return toSession(document);
    }

    private MapSession toSession(Document document) {
        MapSession session = toMapSession(document.entrySet());
        deserializeAttributes(document, session);
        return session;
    }

    private void deserializeAttributes(Document document, MapSession session) {
        Object sessionAttributes = document.get(ATTRIBUTES);
        Map<String, Object> attributes = (Map<String, Object>) this.deserializer.convert(toBytes(sessionAttributes));
        if (attributes != null) {
            attributes.entrySet().forEach((entry) -> {
                session.setAttribute(entry.getKey(), entry.getValue());
                log.debug("deserialize mongodb document => {} : {}", entry.getKey(), entry.getValue());
            });
        }
    }

    private byte[] toBytes(Object sessionAttributes) {
        return (sessionAttributes instanceof Binary ? ((Binary) sessionAttributes).getData()
                : (byte[]) sessionAttributes);
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

    private String resolveUserId(MapSession session) {
        Object authentication = session.getAttribute(SPRING_SECURITY_CONTEXT);
        return authenticationParser.extractName(authentication);
    }

    public static class AuthenticationParser {

        private static final String NAME_EXPRESSION = "authentication?.name";

        private static final SpelExpressionParser PARSER = new SpelExpressionParser();

        private AuthenticationParser() {
        }

        /**
         * Extracts principal name from authentication.
         *
         * @param authentication Authentication object
         * @return principal name
         */
        private String extractName(Object authentication) {
            if (authentication == null) {
                return null;
            }
            Expression expression = PARSER.parseExpression(NAME_EXPRESSION);
            return expression.getValue(authentication, String.class);
        }

    }
    
    private void printSeessionAttributes(MapSession session){
        log.debug("print session {}", session);
        if(session == null){
            return;
        }
        Set<String> attributeNames = session.getAttributeNames();
        log.debug("session {} : {}", SESSION_ID, session.getId());
        log.debug("session {} : {}", CREATION_TIME, session.getCreationTime());
        log.debug("session {} : {}", LAST_ACCESSED_TIME, session.getLastAccessedTime());
        log.debug("session {} : {}", MAX_INACTIVE_INTERVAL, session.getMaxInactiveIntervalInSeconds());
        for(String name : attributeNames){
            Object value = session.getAttribute(name);
            log.debug("session {} : {}",name, value);
        }
    }
}
