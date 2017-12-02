/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons;

import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.security.ClassPathDERFileRSAKeyPairsAdapter;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/02
 */
public class IntegrationTestBase {

    @MockBean
    private HostUrlProvider hostUrlProvider;

    @Component
    public static class RSAKeyPairsStub extends ClassPathDERFileRSAKeyPairsAdapter {

        @Override
        protected String getPrivateKeyPath() {
            return "/key/private-key.der";
        }

        @Override
        protected String getPublicKeyPath() {
            return "/key/public-key.der";
        }
    }

}
