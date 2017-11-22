/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.converter;

import com.pamarin.oauth2.model.AuthorizationRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/18
 */
@FunctionalInterface
public interface HttpServletRequest2AuthorizationRequestConverter {

    AuthorizationRequest convert(HttpServletRequest httpReq);

}
