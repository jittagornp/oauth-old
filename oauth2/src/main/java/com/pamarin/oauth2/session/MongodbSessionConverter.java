/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

import com.mongodb.DBObject;
import org.bson.Document;

/**
 *
 * @author jitta
 */
public interface MongodbSessionConverter {

    DBObject sessionToDBObject(UserSession session);

    UserSession documentToSession(Document document);

}
