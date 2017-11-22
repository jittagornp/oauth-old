/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/03
 */
@FunctionalInterface
public interface ScopeVerification {

    void verifyByClientIdAndScope(String clientId, String scope);

}
