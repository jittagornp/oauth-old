/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security.hashing;

/**
 * @author jitta
 */
public class HmacSHA384Hashing extends HmacHashing {

    public HmacSHA384Hashing(String privateKey) {
        super("HmacSHA384", privateKey);
    }

}
