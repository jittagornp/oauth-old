/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.domain;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
public interface IOAuth2Token extends Cloneable{

    String getId();
    
    void setId(String id);
    
    long getIssuedAt();
    
    void setIssuedAt(long issuedAt);
    
    long getExpiresAt();
    
    void setExpiresAt(long expiresAt);
    
    int getExpireMinutes();
    
    void setExpireMinutes(int minutes);
    
    Object clone() throws CloneNotSupportedException;

}
