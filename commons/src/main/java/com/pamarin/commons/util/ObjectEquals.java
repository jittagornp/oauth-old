/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

import org.springframework.util.Assert;

/**
 *
 * @author jitta
 * @param <T>
 */
public class ObjectEquals<T> {

    private final T origin;

    private ObjectEquals(T origin) {
        Assert.notNull(origin, "require origin.");
        this.origin = origin;
    }

    public static <T> ObjectEquals<T> of(T origin) {
        return new ObjectEquals(origin);
    }

    public boolean equals(Object obj, Callback<T> callback) {
        if (origin == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (origin.getClass() != obj.getClass()) {
            return false;
        }
        final T other = (T) obj;
        return callback.equals(origin, other);
    }

    public static interface Callback<T> {

        boolean equals(T origin, T other);

    }
}
