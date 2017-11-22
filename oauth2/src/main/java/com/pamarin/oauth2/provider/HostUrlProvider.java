/*
 * Copyright 2017 Pamarin.com
 */

package com.pamarin.oauth2.provider;

/**
 * @author jittagornp <http://jittagornp.me>  
 * create : 2017/10/11
 */
@FunctionalInterface
public interface HostUrlProvider {

    String provide();
    
}
