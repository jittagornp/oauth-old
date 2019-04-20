/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author jitta
 */
@Slf4j
public class MongodbSessionRepository implements SessionRepository<MapSession> {

    private static final String COLLECTION_NAME = "mongo_session";

    private static final String ID = "id";
    private static final String OBJECT_ID = "sessionId";
    private static final String CREATION_TIME = "creationTime";
    private static final String LAST_ACCESSED_TIME = "lastAccessedTime";
    private static final String MAX_INTERVAL = "maxInactiveInterval";
    private static final String AGENT_ID = "agentId";
    private static final String USER_ID = "userId";
    private static final String ATTRIBUTES = "attributes";

    private final int maxInactiveIntervalInSeconds = 1800;

    private final MongoOperations mongoOperations;

    private final Converter<Object, byte[]> serializer;
    private final Converter<byte[], Object> deserializer;

    public MongodbSessionRepository(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
        this.serializer = new SerializingConverter();
        this.deserializer = new DeserializingConverter();
    }

    @Override
    public MapSession createSession() {
        MapSession session = new MapSession();
        session.setAttribute(OBJECT_ID, ObjectId.get());
        session.setMaxInactiveIntervalInSeconds(maxInactiveIntervalInSeconds);
        log.debug("create session => {}", session.getId());
        return session;
    }

    @Override
    public void save(MapSession session) {
        log.debug("save session => {}", session.getId());
        mongoOperations.save(toDBObject(session), COLLECTION_NAME);
    }

    @Override
    public MapSession getSession(String id) {
        log.debug("get session => {}", id);
        Document document = mongoOperations.findById(id, Document.class, COLLECTION_NAME);
        if (document == null) {
            return null;
        }
        return toSession(document);
    }

    @Override
    public void delete(String id) {
        log.debug("delete session => {}", id);
        Query query = Query.query(Criteria.where(ID).is(id));
        mongoOperations.remove(query, COLLECTION_NAME);
    }

    private DBObject toDBObject(MapSession session) {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put(ID, session.getId());
        basicDBObject.put(CREATION_TIME, session.getCreationTime());
        basicDBObject.put(LAST_ACCESSED_TIME, session.getLastAccessedTime());
        basicDBObject.put(MAX_INTERVAL, session.getMaxInactiveIntervalInSeconds());
        basicDBObject.put(ATTRIBUTES, serializeAttributes(session));
        return basicDBObject;
    }

    private byte[] serializeAttributes(MapSession session) {
        Map<String, Object> attributes = new HashMap<>();
        session.getAttributeNames().forEach((attrName) -> {
            attributes.put(attrName, session.getAttribute(attrName));
        });
        return this.serializer.convert(attributes);
    }

    private MapSession toSession(Document document) {
        MapSession session = new MapSession();
        session.setId(document.getString(ID));
        session.setCreationTime(document.getLong(CREATION_TIME));
        session.setLastAccessedTime(document.getLong(LAST_ACCESSED_TIME));
        session.setMaxInactiveIntervalInSeconds(document.getInteger(MAX_INTERVAL));
        deserializeAttributes(document, session);
        return session;
    }

    private void deserializeAttributes(Document document, MapSession session) {
        Object sessionAttributes = document.get(ATTRIBUTES);
        Map<String, Object> attributes = (Map<String, Object>) this.deserializer.convert(toBytes(sessionAttributes));
        if (attributes != null) {
            attributes.entrySet().forEach((entry) -> {
                session.setAttribute(entry.getKey(), entry.getValue());
            });
        }
    }

    private byte[] toBytes(Object sessionAttributes) {
        return (sessionAttributes instanceof Binary ? ((Binary) sessionAttributes).getData()
                : (byte[]) sessionAttributes);
    }
}
