/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

/**
 *
 * @author jitta
 */
public interface AESEncryption {

    byte[] encrypt(byte[] data, String secretKey);

    byte[] decrypt(byte[] data, String secretKey);

}
