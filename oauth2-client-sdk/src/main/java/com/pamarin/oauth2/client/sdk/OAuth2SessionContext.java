/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.provider.DefaultHttpServletRequestProvider;
import com.pamarin.commons.provider.HttpServletRequestProvider;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jitta
 */
public class OAuth2SessionContext {

    private static HttpServletRequestProvider httpServletRequestProvider;

    private OAuth2SessionContext() {

    }

    private static HttpServletRequestProvider getHttpServletRequestProvider() {
        if (httpServletRequestProvider == null) {
            httpServletRequestProvider = new DefaultHttpServletRequestProvider();
        }
        return httpServletRequestProvider;
    }

    public static OAuth2Session getSession() {
        HttpServletRequest httpReq = getHttpServletRequestProvider().provide();
        return (OAuth2Session) httpReq.getAttribute(OAuth2SdkConstant.OAUTH2_SESSION);
    }
    
    public static void setSession(OAuth2Session session){
        HttpServletRequest httpReq = getHttpServletRequestProvider().provide();
        httpReq.setAttribute(OAuth2SdkConstant.OAUTH2_SESSION, session);
    }
}
