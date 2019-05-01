/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import static com.pamarin.oauth2.session.CustomSession.Attribute.*;
import java.util.Map;
import org.bson.Document;
import org.bson.types.Binary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;

/**
 *
 * @author jitta
 */
public class DefaultMongodbSessionConverter implements MongodbSessionConverter {

    private final Converter<Object, byte[]> serializer;
    private final Converter<byte[], Object> deserializer;
    private final CustomSessionConverter sessionConverter;

    public DefaultMongodbSessionConverter() {
        this.sessionConverter = new DefaultCustomSessionConverter();
        this.serializer = new SerializingConverter();
        this.deserializer = new DeserializingConverter();
    }

    @Override
    public DBObject sessionToDBObject(CustomSession session) {
        BasicDBObject obj = new BasicDBObject();
        obj.put(SESSION_ID, session.getId());
        obj.put(CREATION_TIME, session.getCreationTime());
        obj.put(MAX_INACTIVE_INTERVAL, session.getMaxInactiveIntervalInSeconds());
        obj.put(LAST_ACCESSED_TIME, session.getLastAccessedTime());
        obj.put(EXPIRATION_TIME, session.getExpirationTime());
        obj.put(AGENT_ID, session.getAgentId());
        obj.put(USER_ID, session.getUserId());
        obj.put(IP_ADDRESS, session.getIpAddress());
        obj.put(ATTRIBUTES, serializeAttributes(session));
        return obj;
    }

    private byte[] serializeAttributes(CustomSession session) {
        return this.serializer.convert(session.getAttributes());
    }

    @Override
    public CustomSession documentToSession(Document document) {
        CustomSession session = sessionConverter.entriesToSession(document.entrySet());
        deserializeAttributes(document, session);
        return session;
    }

    private void deserializeAttributes(Document document, CustomSession session) {
        Object obj = document.get(ATTRIBUTES);
        Map<String, Object> attrs = (Map<String, Object>) this.deserializer.convert(toBytes(obj));
        if (attrs != null) {
            attrs.entrySet().forEach(entry -> session.setAttribute(entry.getKey(), entry.getValue()));
        }
    }

    private byte[] toBytes(Object sessionAttributes) {
        return (sessionAttributes instanceof Binary ? ((Binary) sessionAttributes).getData()
                : (byte[]) sessionAttributes);
    }
}
