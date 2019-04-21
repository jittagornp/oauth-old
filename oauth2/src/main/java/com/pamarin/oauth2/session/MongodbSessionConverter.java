/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import com.mongodb.DBObject;
import org.bson.Document;
import org.springframework.session.MapSession;

/**
 *
 * @author jitta
 */
public interface MongodbSessionConverter {

    DBObject sessionToDBObject(MapSession session);

    MapSession documentToSession(Document document);

}
