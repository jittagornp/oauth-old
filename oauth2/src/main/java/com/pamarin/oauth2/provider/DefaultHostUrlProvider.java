/*
 * Copyright 2017 Pamarin.com
 */

package com.pamarin.oauth2.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt;  
 * create : 2017/11/12
 */
@Component
public class DefaultHostUrlProvider implements HostUrlProvider {

    @Value("${server.hostUrl}")
    private String hostUrl; 
    
    @Override
    public String provide() {
        return hostUrl;
    }

}
