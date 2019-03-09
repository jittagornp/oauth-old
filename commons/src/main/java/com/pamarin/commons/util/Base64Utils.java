/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

import java.util.Base64;

/**
 *
 * @author jitta
 */
public class Base64Utils {

    private Base64Utils() {

    }

    public static String encode(byte[] data) {
        if (data == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(data);
    }

    public static String encode(String data) {
        if (data == null) {
            return null;
        }
        return encode(data.getBytes());
    }

    public static String decode(byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return new String(Base64.getDecoder().decode(data));
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static String decode(String data) {
        if (data == null) {
            return null;
        }
        return decode(data.getBytes());
    }
}
