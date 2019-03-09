/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security.hashing;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/29
 */
public interface Hashing {

    String hash(byte[] data);

    boolean matches(byte[] data, String token);
}
