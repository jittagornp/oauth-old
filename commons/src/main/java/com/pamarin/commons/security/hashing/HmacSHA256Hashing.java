/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security.hashing;

/**
 * @author jitta
 */
public class HmacSHA256Hashing extends HmacHashing {

    public HmacSHA256Hashing(String privateKey) {
        super("HmacSHA256", privateKey);
    }

}
