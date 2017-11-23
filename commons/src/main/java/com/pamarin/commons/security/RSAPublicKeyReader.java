/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import java.io.InputStream;
import java.security.interfaces.RSAPublicKey;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/11
 */
@FunctionalInterface
public interface RSAPublicKeyReader {

    RSAPublicKey readFromDERFile(InputStream inputStream);

}
