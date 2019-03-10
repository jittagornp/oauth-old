/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security.hashing;

import java.lang.reflect.Method;
import org.springframework.cglib.proxy.Proxy;

/**
 *
 * @author jitta
 */
public class ShortHashing extends AbstractHashing {

    private final Hashing hashing;

    private final int length;

    public ShortHashing(Hashing hashing, int length) {
        this.length = length;
        if (length < 32) {
            throw new IllegalArgumentException("length must more than or equals 32.");
        }

        this.hashing = (Hashing) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hashing.class},
                (Object proxy, Method method, Object[] args) -> {
                    if (method.getName().equals("hash")) {
                        return subString((String) method.invoke(hashing, args));
                    }

                    return method.invoke(hashing, args);
                });
    }

    private String subString(String token) {
        if (token == null) {
            return null;
        }
        if (token.length() <= length) {
            return token;
        }
        return token.substring(0, length);
    }

    @Override
    public String hash(byte[] data) {
        return hashing.hash(data);
    }

}
