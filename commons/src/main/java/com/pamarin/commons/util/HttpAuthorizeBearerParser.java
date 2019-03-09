/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
@Component
public interface HttpAuthorizeBearerParser {

    String parse(String authorization);

}
