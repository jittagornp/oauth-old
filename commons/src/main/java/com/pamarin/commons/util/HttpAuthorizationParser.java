/*
 * Copyright 2017 Pamarin.com
 */

package com.pamarin.commons.util;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt;  
 * create : 2017/12/03
 */
@FunctionalInterface
public interface HttpAuthorizationParser {

    String parse(String type, String authorization);
    
}
