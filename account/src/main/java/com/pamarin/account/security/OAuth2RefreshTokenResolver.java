/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.account.security;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jitta
 */
public interface OAuth2RefreshTokenResolver {

    String resolve(HttpServletRequest httpReq);
    
    String getTokenName();

}
