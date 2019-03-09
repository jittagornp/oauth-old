/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/29
 */
@FunctionalInterface
public interface ClientService {

    String findClientSecretByClientId(String clientId);

}
