/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pamarin.commons.resolver.DefaultPrincipalNameResolver;
import com.pamarin.commons.resolver.PrincipalNameResolver;
import static com.pamarin.oauth2.session.SessionAttributeConstant.*;
import java.util.Map;
import org.bson.Document;
import org.bson.types.Binary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.session.MapSession;

/**
 *
 * @author jitta
 */
public class DefaultMongodbSessionConverter implements MongodbSessionConverter {

    private final Converter<Object, byte[]> serializer;
    private final Converter<byte[], Object> deserializer;
    private final SessionConverter sessionConverter;
    private final PrincipalNameResolver principalNameResolver;

    public DefaultMongodbSessionConverter() {
        this.sessionConverter = new DefaultSessionConverter();
        this.serializer = new SerializingConverter();
        this.deserializer = new DeserializingConverter();
        this.principalNameResolver = new DefaultPrincipalNameResolver();
    }

    @Override
    public DBObject sessionToDBObject(MapSession session) {
        BasicDBObject obj = new BasicDBObject();
        obj.put(SESSION_ID, session.getId());
        obj.put(CREATION_TIME, session.getCreationTime());
        obj.put(MAX_INACTIVE_INTERVAL, session.getMaxInactiveIntervalInSeconds());
        obj.put(LAST_ACCESSED_TIME, session.getLastAccessedTime());
        obj.put(EXPIRATION_TIME, session.getAttribute(EXPIRATION_TIME));
        obj.put(AGENT_ID, session.getAttribute(AGENT_ID));
        obj.put(USER_ID, principalNameResolver.resolve(session));
        obj.put(IP_ADDRESS, session.getAttribute(IP_ADDRESS));
        obj.put(ATTRIBUTES, serializeAttributes(session));
        return obj;
    }

    private byte[] serializeAttributes(MapSession session) {
        return this.serializer.convert(sessionConverter.getSessionAttributes(session));
    }

    @Override
    public MapSession documentToSession(Document document) {
        MapSession session = sessionConverter.entriesToSession(document.entrySet());
        deserializeAttributes(document, session);
        return session;
    }

    private void deserializeAttributes(Document document, MapSession session) {
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
