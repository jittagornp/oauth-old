/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.constant;

import static com.pamarin.commons.util.ClassUtils.isPrivateConstructor;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author jitta
 */
public class OAuth2ConstantTest {
    
    @Test
    public void shouldBePrivateConstructor(){
        assertTrue(isPrivateConstructor(OAuth2Constant.class));
    }
    
}
