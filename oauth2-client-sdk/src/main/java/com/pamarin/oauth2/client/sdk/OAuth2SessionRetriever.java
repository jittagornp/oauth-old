/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
