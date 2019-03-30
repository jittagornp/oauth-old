/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jitta
 */
public interface OAuth2SessionRetriever {

    void retrieve(HttpServletRequest httpReq, HttpServletResponse httpResp);

}
