/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.session;

/**
 *
 * @author jitta
 */
public final class SessionAttributeConstant {

    //follow session spec
    public static final String SESSION_ID = "sessionId"; //is id
    public static final String CREATION_TIME = "creationTime";
    public static final String LAST_ACCESSED_TIME = "lastAccessedTime";
    public static final String MAX_INACTIVE_INTERVAL = "maxInactiveInterval";
    
    //additional
    public static final String AGENT_ID = "agentId";
    public static final String USER_ID = "userId";
    public static final String IP_ADDRESS = "ipAddress";
    public static final String ATTRIBUTES = "attrs";
    
    private SessionAttributeConstant(){
        
    }

}
