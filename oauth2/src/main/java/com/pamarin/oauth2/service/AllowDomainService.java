/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import java.util.List;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/29
 */
@FunctionalInterface
public interface AllowDomainService {

    List<String> findDomainByClientId(String clientId);

}
