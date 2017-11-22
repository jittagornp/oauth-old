/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.model;

import java.util.List;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/23
 */
public interface ClientDetails {

    String getClientId();

    String getClientSecret();

    String getRedirectUri();

    List<String> getScopes();

}
