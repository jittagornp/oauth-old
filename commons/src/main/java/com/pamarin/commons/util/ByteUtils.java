/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.util;

import com.pamarin.commons.exception.UnsupportedPrivateConstructorException;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/18
 */
public class ByteUtils {

    private ByteUtils() {
        throw new UnsupportedPrivateConstructorException();
    }

    public static byte[] xor(byte[] data1, byte[] data2) {
        // make data2 the largest...
        if (data1.length > data2.length) {
            byte[] tmp = data2;
            data2 = data1;
            data1 = tmp;
        }

        for (int i = 0; i < data1.length; i++) {
            data2[i] ^= data1[i];
        }

        return data2;
    }

}
