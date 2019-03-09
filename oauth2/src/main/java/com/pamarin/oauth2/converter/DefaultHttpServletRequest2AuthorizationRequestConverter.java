/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.converter;

import com.pamarin.oauth2.model.AuthorizationRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/18
 */
@Component
public class DefaultHttpServletRequest2AuthorizationRequestConverter implements HttpServletRequest2AuthorizationRequestConverter {

    @Override
    public AuthorizationRequest convert(HttpServletRequest httpReq) {
        return AuthorizationRequest.builder()
                .responseType(httpReq.getParameter("response_type"))
                .clientId(httpReq.getParameter("client_id"))
                .redirectUri(httpReq.getParameter("redirect_uri"))
                .scope(httpReq.getParameter("scope"))
                .state(httpReq.getParameter("state"))
                .build();
    }

}
