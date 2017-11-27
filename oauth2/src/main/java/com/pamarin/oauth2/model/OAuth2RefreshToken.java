/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Getter
@Setter
@Builder
public class OAuth2RefreshToken implements Cloneable {

    private String id;

    private String userId;

    private String username;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); 
    }

}
