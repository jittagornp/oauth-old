/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import static java.lang.reflect.Modifier.isPrivate;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/23
 */
public class ClassUtils {

    private ClassUtils() {

    }

    public static boolean isPrivateConstructor(Class<?> clazz) {
        Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        if (isPrivate(constructor.getModifiers())) {
            constructor.setAccessible(true);
            try {
                constructor.newInstance();
                return true;
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
        return false;
    }

}
