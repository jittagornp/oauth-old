/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/22
 */
public interface PasswordEncryption {

    boolean matches(String rawPassword, String encryptedPassword);
    
    String encrypt(String rawPassword);
    
}
