/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/29
 */
public interface CheckSum {

    String hash(byte[] data);

    boolean matches(byte[] data, String token);
}
