/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public interface Base64AESEncryption {
    
    String encrypt(String data, String secretKey);
    
    String decrypt(String data, String secretKey);
    
}
