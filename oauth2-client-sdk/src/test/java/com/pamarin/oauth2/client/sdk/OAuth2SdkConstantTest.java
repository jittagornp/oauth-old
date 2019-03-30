/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.util.ClassUtils;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author jitta
 */
public class OAuth2SdkConstantTest {
    
    @Test
    public void shouldBePrivateConstructor(){
        assertTrue(ClassUtils.isPrivateConstructor(OAuth2SdkConstant.class));
    }
    
}
