/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
@Component
public class DefaultHttpAuthorizeBearerParser implements HttpAuthorizeBearerParser {

    private final HttpAuthorizationParser parser;
    
    @Autowired
    public DefaultHttpAuthorizeBearerParser(HttpAuthorizationParser parser) {
        this.parser = parser;
    }

    @Override
    public String parse(String authorization) {
        return parser.parse("bearer", authorization);
    }

}
