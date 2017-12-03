/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
@Component
public class DefaultHttpAuthorizeBearerParser implements HttpAuthorizeBearerParser {

    @Autowired
    private HttpAuthorizationParser parser;

    @Override
    public String parse(String authorization) {
        return parser.parse("bearer", authorization);
    }

}
