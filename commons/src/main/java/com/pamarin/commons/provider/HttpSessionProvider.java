/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.provider;

import javax.servlet.http.HttpSession;

/**
 *
 * @author jitta
 */
public interface HttpSessionProvider {
    
    HttpSession provide();
    
}
