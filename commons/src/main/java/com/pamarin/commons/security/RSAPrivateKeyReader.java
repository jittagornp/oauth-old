/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/11
 */
@FunctionalInterface
public interface RSAPrivateKeyReader {

    RSAPrivateKey readFromDERFile(InputStream inputStream);

}
